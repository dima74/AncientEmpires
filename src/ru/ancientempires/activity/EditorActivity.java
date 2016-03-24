package ru.ancientempires.activity;

import android.os.Bundle;
import android.view.View;
import ru.ancientempires.Extras;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.IDraw;
import ru.ancientempires.editor.EditorView;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSnapshotLoader;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class EditorActivity extends BaseActivity
{
	
	public static EditorActivity	activity;
									
	public String					gameID;
	public Game						game;
	public View						view;
									
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		gameID = getIntent().getStringExtra(Extras.GAME_ID);
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		new MyAsyncTask(this)
		{
			@Override
			public void doInBackground() throws Exception
			{
				Client.client.finishPart2();
				GamePath path = Client.getGame(gameID);
				Rules rules = path.getRules();
				game = new GameSnapshotLoader(path, rules).load(false);
				Client.client.images.load(Client.client.imagesLoader, game);
				IDraw.gameStatic = game;
			}
			
			@Override
			public void onPostExecute()
			{
				activity = EditorActivity.this;
				view = new EditorView(EditorActivity.this);
				setContentView(view);
			};
		}.start();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		activity = null;
		game = null;
		IDraw.gameStatic = null;
	}
	
}
