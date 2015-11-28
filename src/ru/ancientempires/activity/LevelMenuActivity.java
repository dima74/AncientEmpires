package ru.ancientempires.activity;

import java.io.IOException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.GameInit;
import ru.ancientempires.Localization;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.Images;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;

public class LevelMenuActivity extends ListActivity
{
	
	public GamesFolder	currentFolder;
	private boolean		isStartGame	= false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_menu_list_view);
		
		Intent intent = getIntent();
		currentFolder = GamesFolder.getFolder(intent.getStringExtra(PlayMenuActivity.EXTRA_FOLDER));
		setTitle(currentFolder.name);
		
		if (!GameInit.foldersInitThread.isAlive())
		{
			start();
			return;
		}
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.loading));
		dialog.setCancelable(false);
		dialog.show();
		
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Localization.load("games/strings");
					GameInit.foldersInitThread.join();
				}
				catch (InterruptedException | IOException e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						dialog.dismiss();
						start();
					}
					
				});
			}
		}).start();
	}
	
	public void start()
	{
		String[] values;
		if (currentFolder.folders != null)
		{
			values = new String[currentFolder.folders.length];
			for (int i = 0; i < currentFolder.folders.length; i++)
				values[i] = currentFolder.folders[i].name;
		}
		else
		{
			values = new String[currentFolder.games.length];
			for (int i = 0; i < currentFolder.games.length; i++)
				values[i] = Localization.get(currentFolder.games[i].gameID + ".name");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_item, R.id.text_view, values);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		if (isStartGame)
			return;
		isStartGame = true;
		if (currentFolder.folders != null)
		{
			currentFolder = currentFolder.folders[position];
			start();
		}
		else
		{
			final ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getString(R.string.loading));
			dialog.setCancelable(false);
			dialog.show();
			
			final GamePath gamePath = currentFolder.games[position];
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						GameInit.initThread.join();
						Client.getClient().startGame(gamePath.gameID);
						Images.loadResources(Client.getClient().getGame());
					}
					catch (InterruptedException | IOException e)
					{
						MyAssert.a(false);
						e.printStackTrace();
					}
					
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							dialog.dismiss();
							startActivity(new Intent().setClass(LevelMenuActivity.this, GameActivity.class));
							isStartGame = false;
						}
					});
				}
			}).start();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_menu, menu);
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
