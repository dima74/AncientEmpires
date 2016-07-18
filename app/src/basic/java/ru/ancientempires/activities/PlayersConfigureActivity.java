package ru.ancientempires.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.TreeSet;

import ru.ancientempires.Extras;
import ru.ancientempires.Localization;
import ru.ancientempires.R;
import ru.ancientempires.Strings;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.PlayerType;
import ru.ancientempires.model.Team;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class PlayersConfigureActivity extends BaseActivity
{
	
	/*
	
	Снимок - полное состояние игры в какой-то момент времени.
	Сохранение - несколько таких снимков с записанными переходами между ними.
	Базовая игра - это снимок.
	При старте новой игры получается сохранение.
	
	Предположительно у всех снимков сохранения должны быть одинаковыми следующие параметры:
		высота и ширина карты
		число команд и число игроков
		типы игроков
		strings.json
		campaign.json
	Но, возможно, типы игроков всё-таки будут меняться,
		например играют по сети три игрока, у одного оборвалась связь,
		а два других хотят заменить его на бота и продолжить игру.
	Поэтому для простоты неизменным будем счтитаь только strings.json и campaign.json
	
	Старт новой игры:
		Мы не можем менять содержимое папки откуда взяли игру, поэтому нужно создать новую папку.
		В ней должна быть информация о игре из которой она была создана (базовой игре)
			Зачем? Чтобы можно было начать игру заново.
			Соответственно нужно как-то запомнить команды,
				чтобы предложить их в качестве стандартных при рестарте.
	Сохранение игры:
		Новое сохранение появляется только при старте новой игры.
		Сохранение должно быть инкрементальным, то есть надо записывать каждое действие.
		Так же раз в ~100 действий должен быть записан снимок игры,
			чтобы можно было быстро отменять действия,
			ну и чтобы при загрузке игры приходилось выполнять заново не более 100 действий.
			Почему нельзя сохранить последний снимок игры?
				Потому что это может занимать длительное времяЮ и он может не сохраниться
					(юзер играет, играет, потом резко нажимает кнопку все приложения и завершает наше)
		Соответственно при старте игры базовая игра должна быть скопирована в папку сохранения,
			она станет первым снимком сохранения.
	Загрузка игры:
		Должен быть список всех сохранений. Просто отображаем его.
	Редактор карт:
		Должна быть возможность выбрать из всех снимков
			кампаний/мультиплеера/последних снимков сохранений.
		Вместе со снимком должны быть загружены campaign.json и strings.json
		В результате редактирования получается базовая игра: снимок + campaign & strings
	Обмен картами по сети:
		Должна быть возможность обмениваться как картами созданными в редакторе карт (базовая игра),
			так и сохранениями.
	Загрузка не последнего состояния сохранения, а промежуточного:
		Нужно уметь получать снимок игры по последнему подходящему снимку и переходам.
	
	Старт новой игры:
		1. Если можно --- настроить параметры игроков, основываясь на записанных в последнем снимке
			(ну или на стандартных для текущих правил)
				(такие как типы игроков, золото, команды и ограничение числа войнов)
					они могут понадобиться только в случае рестарта этой же игры, поэтому можем их не сохранять, а явно передать
		2. Создать папку куда будем сохранять этот экземпляр игры,
			загрузить игру из базовой папки,
			сохранить игру в свою папку,
			в папке сохранения - добавить в info.json информацию о
				базовой папке и дате последнего изменения + ещё что-то вроде e-mail'а автора.
		3. Настроить инкрементальное сохранение
	 */
	
	private GamePath path;
	private Player[] players;
	private View[]   views;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.choose_view);

			String gameID = getIntent().getStringExtra(Extras.GAME_ID);
			path = Client.getGame(gameID);
			setTitle(path.name);

			String lastPlayers = getIntent().getStringExtra(Extras.LAST_PLAYERS);
			Client.client.finishPart2();
			Game game = path.loadGame(false);
			game.setNumberTeams(game.numberPlayers());
			JsonArray playersJson = (JsonArray) (lastPlayers != null ? new JsonParser().parse(lastPlayers) : game.toJson().get("players"));
			players = Player.fromJsonArray(playersJson, game.getLoaderInfo());
			for (int i = 0; i < players.length; i++)
				players[i].ordinal = i;

			((ImageView) findViewById(R.id.imageView)).setImageBitmap(getBitmap(game));

			views = new View[this.players.length];
			LinearLayout listView = (LinearLayout) findViewById(R.id.listView);
			listView.removeAllViews();
			for (Player player : players)
				listView.addView(views[player.ordinal] = getView(player, player.team.ordinal, players.length));

			TextView textGold = (TextView) findViewById(R.id.textGold);
			textGold.setText(Strings.GOLD.toString());
			EditText textGoldEdit = (EditText) findViewById(R.id.textGoldEdit);
			textGoldEdit.setHint(String.valueOf(players[0].gold));
			textGoldEdit.addTextChangedListener(new MyTextWatcher(700000));

			TextView textUnitsLimit = (TextView) findViewById(R.id.textUnitsLimit);
			textUnitsLimit.setText(Strings.UNITS_LIMIT.toString());
			EditText textUnitsLimitEdit = (EditText) findViewById(R.id.textUnitsLimitEdit);
			new MyTextWatcher(700).addTo(textUnitsLimitEdit, players[0].unitsLimit);

			if (MainActivity.firstStart)
				onClick();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

	private Bitmap getBitmap(Game game)
	{
		try
		{
			Client.client.images.load(Client.client.imagesLoader, game);
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}

		BaseGameActivity.activity = new BaseGameActivity();
		BaseGameActivity.activity.game = game;
		BaseDrawMain mainBase = new BaseDrawMain()
		{
			@Override
			public void setVisibleMapSize()
			{}
		};
		mainBase.iMax = game.h;
		mainBase.jMax = game.w;

		int h = game.h * 24;
		int w = game.w * 24;
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		mainBase.cells.draw(canvas);
		mainBase.cellsDual.draw(canvas);
		mainBase.unitsDead.draw(canvas);
		mainBase.units.draw(canvas);
		BaseGameActivity.activity = null;
		BaseDrawMain.mainBase = null;

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int screenW = metrics.widthPixels;
		int screenH = metrics.widthPixels;
		int scale = 1;
		while (w * scale < screenW && h * scale < screenH * 2 / 3)
			++scale;
		if (scale > 1)
			bitmap = Bitmap.createScaledBitmap(bitmap, w * scale, h * scale, false);

		return bitmap;
	}
	
	private View getView(Player player, int team, int numberTeams)
	{
		View view = getLayoutInflater().inflate(R.layout.choose_item, null);
		TextView textColor = (TextView) view.findViewById(R.id.textColor);
		Spinner spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		Spinner spinnerTeam = (Spinner) view.findViewById(R.id.spinnerTeam);
		
		textColor.setText(Localization.get(player.color.name()));
		textColor.setTextColor(player.color.showColor);
		
		ArrayAdapter<PlayerType> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, PlayerType.values());
		spinnerType.setAdapter(adapterType);
		spinnerType.setSelection(player.type.ordinal());
		
		String[] teamsNames = new String[numberTeams];
		for (int i = 0; i < teamsNames.length; i++)
			teamsNames[i] = Strings.TEAM.toString() + " " + (i + 1);
		ArrayAdapter<String> adapterTeam = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, teamsNames);
		spinnerTeam.setAdapter(adapterTeam);
		spinnerTeam.setSelection(team);
		
		return view;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.players_configure_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int itemId = item.getItemId();
		switch (itemId)
		{
			case R.id.action_fight:
				onClick();
		}
		return true;
	}

	public void onClick()
	{
		try
		{
			int gold = getIntValue(R.id.textGoldEdit);
			int unitsLimit = getIntValue(R.id.textUnitsLimitEdit);

			Integer[] teamNumbers = new Integer[players.length];
			for (int i = 0; i < players.length; i++)
				teamNumbers[i] = ((Spinner) views[i].findViewById(R.id.spinnerTeam)).getSelectedItemPosition();
			Integer[] teamNumbersSorted = new TreeSet<>(Arrays.asList(teamNumbers)).toArray(new Integer[0]);
			int[] teamNumbersToOrdinals = new int[players.length];
			for (int i = 0; i < teamNumbersSorted.length; i++)
				teamNumbersToOrdinals[teamNumbersSorted[i]] = i;

			for (int i = 0; i < players.length; i++)
			{
				players[i].team = new Team(teamNumbersToOrdinals[teamNumbers[i]]);
				players[i].type = (PlayerType) ((Spinner) views[i].findViewById(R.id.spinnerType)).getSelectedItem();
				players[i].gold = gold;
				players[i].unitsLimit = unitsLimit;
			}

			LevelMenuActivity.activity.finish();
			moveTo(GameActivity.class, new Intent()
					.putExtra(Extras.GAME_ID, path.gameID)
					.putExtra(Extras.PLAYERS, SerializableJsonHelper.toJsonArray(players).toString()));
		}
		catch (Exception e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
		//moveTo(LevelMenuActivity.class, new Intent()
		//		.putExtra(Extras.FOLDER_ID, path.getFolderID())
		//		.putExtra(Extras.FOCUS_ON, path.gameID));
	}
	
}
