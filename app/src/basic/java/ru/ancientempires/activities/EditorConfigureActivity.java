package ru.ancientempires.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.util.ArrayList;
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
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;

public class EditorConfigureActivity extends BaseActivity
{
	
	private static final int MAX_MAP_SIZE = 50;
	private boolean isStarting;
	private String  gameID;
	private ArrayList<View> frequencyViews = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor_configure);
		
		try
		{
			Client.client.finishPart2();
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

		createFrequencies();
	}

	private void createFrequencies()
	{
		CellType[] cellTypes = Client.client.rules.cellTypes;
		int[] frequencies = new int[cellTypes.length];
		for (int i = 0; i < cellTypes.length; i++)
			frequencies[i] = cellTypes[i].mapEditorFrequency;

		LinearLayout frequenciesLayout = (LinearLayout) findViewById(R.id.frequencies);
		frequenciesLayout.removeAllViews();
		for (int i = 0; i < cellTypes.length; i++)
			if (!cellTypes[i].isDefault && frequencies[i] > 0)
			{
				View view = createView(cellTypes[i], frequencies[i]);
				frequencyViews.add(view);
				frequenciesLayout.addView(view);
			}
		
		ImageView buttonAdd = (ImageView) findViewById(R.id.imageAdd);
		buttonAdd.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});
	}

	@NonNull
	private View createView(CellType cellType, int frequency)
	{
		View view = getLayoutInflater().inflate(R.layout.frequency_item, null);

		EditText edit = (EditText) view.findViewById(R.id.textFrequency);
		edit.setHint("0");
		edit.setText("" + frequency);

		ImageView image = (ImageView) view.findViewById(R.id.cellImage);
		image.setImageBitmap(Client.client.images.cell.cellBitmaps[cellType.ordinal].defaultBitmap.getBitmap());
		image.setTag(cellType);
		return view;
	}

	public void onClick()
	{
		if (isStarting)
			return;
		isStarting = true;
		new MyAsyncTask(this)
		{
			@Override
			public void doInBackground() throws Exception
			{
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
		int[] frequencies = getFrequencies();

		MyAssert.a(Client.client.rules != null);
		// TODO new Game().fromJson(rules.defaultGame)
		Game game = new Game(Client.client.rules)
				.setSize(h, w, frequencies)
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

	private int[] getFrequencies()
	{
		CellType[] cellTypes = Client.client.rules.cellTypes;
		int[] frequencies = new int[cellTypes.length];
		for (View view : frequencyViews)
		{
			CellType cellType = (CellType) view.findViewById(R.id.cellImage).getTag();
			int frequency = getIntValue(view, R.id.textFrequency);
			frequencies[cellType.ordinal] = frequency;
		}
		return frequencies;
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
		if (id == R.id.action_create)
		{
			onClick();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
