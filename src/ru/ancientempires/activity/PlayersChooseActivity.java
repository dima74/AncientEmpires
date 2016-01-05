package ru.ancientempires.activity;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import ru.ancientempires.Localization;
import ru.ancientempires.PlayerType;
import ru.ancientempires.R;
import ru.ancientempires.SimplePlayer;
import ru.ancientempires.SimpleTeam;
import ru.ancientempires.Strings;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.load.GamePath;

public class PlayersChooseActivity extends Activity implements OnClickListener
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
	Поэтому для простоты в снимке будет всё кроме strings.json и campaign.json
	
	Старт новой игры:
		Мы не можем менять содержимое папки откуда взяли игру, поэтому нужно создать новую папку.
		В ней должна быть информация о игре из которой она была создана (базовой игре)
			Зачем? Чтобы можно было начать игру заново. 
			Соответственно нужно как-то запомнить команды, 
				чтобы предложить их в качестве стандартных при рестарте
	Сохранение игры:
		Новое сохранение появляется только при старте новой игры.
		Сохранение должно быть инкрементальным, то есть надо записывать каждое действие.
		Так же раз в ~100 действий должен быть записан снимок игры, 
			чтобы можно было быстро отменять действия.
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
		1. Если надо - выбрать тимы (основываясь на defaultTeams.json), 
			сохранить их в базовую папку игры (lastTeams.json)
		2. Создать папку куда будем сохранять этот экземпляр игры, 
			загрузить игру из базовой папки,  
			сохранить игру в свою папку, 
			в папке сохранения - добавить в info.json информацию о 
				базовой папке и дате последнего изменения + ещё что-то вроде e-mail'а автора. 
			настроить инкрементальное сохранение.
	 
	 */
	
	private static class MyTextWatcher implements TextWatcher
	{
		private int maxValue;
		
		public MyTextWatcher(int maxValue)
		{
			this.maxValue = maxValue;
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{}
		
		@Override
		public void afterTextChanged(Editable s)
		{
			int value = s.length() == 0 ? 0 : Integer.valueOf(s.toString());
			if (value > maxValue)
				s.replace(0, s.length(), String.valueOf(maxValue));
		}
	}
	
	private GamePath		path;
	private SimpleTeam[]	teams;
	private SimplePlayer[]	players;
	private View[]			views;
	private int				defaultGold;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_view);
		
		final String gameID = getIntent().getStringExtra(GameActivity.EXTRA_GAME_ID);
		boolean useLastTeams = getIntent().getBooleanExtra(GameActivity.EXTRA_USE_LAST_TEAMS, false);
		path = Client.getGame(gameID);
		
		int unitsLimit = -1;
		try
		{
			JsonReader reader = path.getLoader().getReader(useLastTeams ? "lastTeams.json" : "defaultTeams.json");
			reader.beginObject();
			MyAssert.a("teams", reader.nextName());
			teams = new Gson().fromJson(reader, SimpleTeam[].class);
			unitsLimit = JsonHelper.readInt(reader, "unitsLimit");
			reader.endObject();
			reader.close();
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		
		players = new SimplePlayer[path.numberPlayers];
		views = new View[players.length];
		LinearLayout listView = (LinearLayout) findViewById(R.id.listView);
		for (int iTeam = 0; iTeam < teams.length; iTeam++)
			for (SimplePlayer player : teams[iTeam].players)
			{
				players[player.ordinal] = player;
				listView.addView(views[player.ordinal] = getView(player, iTeam));
			}
			
		TextView textGold = (TextView) findViewById(R.id.textGold);
		textGold.setText(Strings.GOLD.toString());
		EditText textGoldEdit = (EditText) findViewById(R.id.textGoldEdit);
		defaultGold = players[0].gold;
		textGoldEdit.setHint(String.valueOf(defaultGold));
		textGoldEdit.addTextChangedListener(new MyTextWatcher(999999));
		
		TextView textUnitsLimit = (TextView) findViewById(R.id.textUnitsLimit);
		textUnitsLimit.setText(Strings.UNITS_LIMIT.toString());
		EditText textUnitsLimitEdit = (EditText) findViewById(R.id.textUnitsLimitEdit);
		textUnitsLimitEdit.setHint(String.valueOf(unitsLimit));
		textUnitsLimitEdit.addTextChangedListener(new MyTextWatcher(100));
		
		Button button = (Button) findViewById(R.id.button);
		button.setText(Strings.FIGHT.toString());
		button.setOnClickListener(this);
		
		onClick(null);
	}
	
	private View getView(SimplePlayer player, int team)
	{
		View view = getLayoutInflater().inflate(R.layout.choose_item, null);
		TextView textColor = (TextView) view.findViewById(R.id.textColor);
		Spinner spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
		Spinner spinnerTeam = (Spinner) view.findViewById(R.id.spinnerTeam);
		
		textColor.setText(Localization.get(player.color.name()));
		// TODO сделать в xml
		textColor.setTextSize(20);
		textColor.setTextColor(player.color.showColor);
		
		ArrayAdapter<PlayerType> adapterType = new ArrayAdapter<PlayerType>(this, android.R.layout.simple_spinner_dropdown_item, PlayerType.values());
		spinnerType.setAdapter(adapterType);
		spinnerType.setSelection(player.type.ordinal());
		
		String[] teamsNames = new String[teams.length];
		for (int i = 0; i < teamsNames.length; i++)
			teamsNames[i] = Strings.TEAM.toString() + " " + (i + 1);
		ArrayAdapter<String> adapterTeam = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, teamsNames);
		spinnerTeam.setAdapter(adapterTeam);
		spinnerTeam.setSelection(team);
		
		return view;
	}
	
	private int getValue(int id)
	{
		EditText editText = (EditText) findViewById(id);
		String text = editText.getText().toString();
		return Integer.valueOf(text.isEmpty() ? editText.getHint().toString() : text);
	}
	
	@Override
	public void onClick(View v)
	{
		int gold = getValue(R.id.textGoldEdit);
		int unitsLimit = getValue(R.id.textUnitsLimitEdit);
		
		ArrayList<SimplePlayer>[] teamsList = new ArrayList[teams.length];
		for (int i = 0; i < teamsList.length; i++)
			teamsList[i] = new ArrayList<SimplePlayer>();
		for (int i = 0; i < views.length; i++)
		{
			View view = views[i];
			Spinner spinnerType = (Spinner) view.findViewById(R.id.spinnerType);
			Spinner spinnerTeam = (Spinner) view.findViewById(R.id.spinnerTeam);
			int iTeam = spinnerTeam.getSelectedItemPosition();
			teamsList[iTeam].add(new SimplePlayer(players[i].color, i, (PlayerType) spinnerType.getSelectedItem(), gold));
		}
		
		ArrayList<SimpleTeam> teams = new ArrayList<SimpleTeam>();
		for (ArrayList<SimplePlayer> list : teamsList)
			if (!list.isEmpty())
				teams.add(new SimpleTeam(list.toArray(new SimplePlayer[0])));
		try
		{
			path.getLoader().mkdirs("");
			JsonWriter writer = path.getLoader().getWriter("lastTeams.json");
			writer.beginObject();
			writer.name("teams");
			new Gson().toJson(teams.toArray(new SimpleTeam[0]), SimpleTeam[].class, writer);
			writer.name("unitsLimit").value(unitsLimit);
			writer.endObject();
			writer.close();
		}
		catch (IOException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		startActivity(new Intent(this, GameActivity.class)
				.putExtra(GameActivity.EXTRA_GAME_ID, path.gameID));
	}
	
}
