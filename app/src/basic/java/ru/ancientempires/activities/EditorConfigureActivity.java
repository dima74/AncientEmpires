package ru.ancientempires.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.util.HashMap;
import java.util.Map;

import ru.ancientempires.Extras;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.R;
import ru.ancientempires.Strings;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSaver;
import ru.ancientempires.model.Game;

public class EditorConfigureActivity extends BaseActivity implements OnClickListener
{
	
	private static final int MAX_MAP_SIZE = 50;
	private boolean isStarting;
	private String  gameID;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_editor_choose);
		
		try
		{
			Client.client.finishPart1();
		}
		catch (InterruptedException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		MyAssert.a(Client.client.user != null);
		setText(R.id.textName, Strings.EDITOR_GAME_NAME);
		setHint(R.id.textNameEdit, Client.client.getNameForNewGame());
		
		setText(R.id.textHeight, Strings.EDITOR_GAME_HEIGHT);
		setText(R.id.textWidth, Strings.EDITOR_GAME_WIDTH);
		new MyTextWatcher(1, MAX_MAP_SIZE).addTo(this, R.id.textHeightEdit, 15);
		new MyTextWatcher(1, MAX_MAP_SIZE).addTo(this, R.id.textWidthEdit, 15);
		
		((Button) findViewById(R.id.button)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (isStarting)
			return;
		isStarting = true;
		new MyAsyncTask(this)
		{
			@Override
			public void doInBackground() throws Exception
			{
				Client.client.finishPart2();
				createGame();
			}

			@Override
			public void onPostExecute()
			{
				moveTo(EditorActivity.class, new Intent().putExtra(Extras.GAME_ID, gameID));
			}
		}.start();
	}

	// сохраняем игру, в GameEditorActivity передаём только её gameId
	private void createGame() throws Exception
	{
		String name = getValue(R.id.textNameEdit);
		int h = getIntValue(R.id.textHeightEdit);
		int w = getIntValue(R.id.textWidthEdit);

		MyAssert.a(Client.client.rules != null);
		// TODO new Game().fromJson(rules.defaultGame)
		Game game = new Game(Client.client.rules)
				.setSize(h, w)
				.setNumberPlayers(4);
		game.campaign.isDefault = true;

		gameID = "user." + Client.client.user.numberGames;
		GamePath path = new GamePath(game, gameID);
		path.isBaseGame = true;
		path.canChooseTeams = true;
		path.getLoader().mkdirs();
		path.getFolder().add(path);
		path.name = name;
		Client.client.allGames.put(gameID, path);
		GameSaver.createBaseGame(game);

		HashMap<String, String> strings = new HashMap<>();
		strings.put("name", name);
		JsonWriter writer = path.getLoader().getWriter("strings.json");
		new Gson().toJson(strings, Map.class, writer);
		writer.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_editor_choose, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
			return true;
		return super.onOptionsItemSelected(item);
	}
	
}
