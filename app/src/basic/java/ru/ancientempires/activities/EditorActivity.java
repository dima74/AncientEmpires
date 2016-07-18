package ru.ancientempires.activities;

import android.os.Bundle;

import ru.ancientempires.Extras;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.client.Client;
import ru.ancientempires.editor.EditorThread;
import ru.ancientempires.editor.EditorView;
import ru.ancientempires.load.GamePath;

public class EditorActivity extends BaseGameActivity
{
	
	// public static EditorActivity activity;
	
	public String gameID;
	
	@Override
	public EditorThread getThread()
	{
		return (EditorThread) super.getThread();
	}
	
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
				game = path.loadGame(false);
				Client.client.images.load(Client.client.imagesLoader, game);
				game.campaign.isDefault = true;
			}
			
			@Override
			public void onPostExecute()
			{
				// activity = EditorActivity.this;
				BaseGameActivity.activity = EditorActivity.this;
				view = new EditorView(EditorActivity.this);
				setContentView(view);
			}
		}.start();
	}

}
