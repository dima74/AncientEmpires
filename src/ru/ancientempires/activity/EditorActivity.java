package ru.ancientempires.activity;

import android.os.Bundle;
import ru.ancientempires.Extras;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.IDraw;
import ru.ancientempires.editor.EditorChooseDialog;
import ru.ancientempires.editor.EditorThread;
import ru.ancientempires.editor.EditorView;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSnapshotLoader;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class EditorActivity extends BaseActivity
{
	
	public static EditorActivity	activity;
									
	public EditorView				view;
	public EditorThread				thread;
									
	public String					gameID;
	public Game						game;
									
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		gameID = getIntent().getStringExtra(Extras.GAME_ID);
		MainActivity.firstStart = false;
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
				game.campaign.isDefault = true;
				IDraw.gameStatic = game;
			}
			
			@Override
			public void onPostExecute()
			{
				activity = EditorActivity.this;
				view = new EditorView(EditorActivity.this);
				thread = (EditorThread) view.thread;
				setContentView(view);
			};
		}.start();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		setContentView(null);
		thread.isRunning = false;
		if (EditorChooseDialog.dialog != null)
		{
			EditorChooseDialog.dialog.dismiss();
			EditorChooseDialog.dialog = null;
		}
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			MyAssert.a(false);
			e.printStackTrace();
		}
		activity = null;
		game = null;
		IDraw.gameStatic = null;
	}
	
}
