package ru.ancientempires.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.Localization;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.Debug;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamesFolder;

public class LevelMenuActivity extends ListActivity
{
	
	public GamesFolder	currentFolder;
	private boolean		isStartingGameInProcess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Debug.create(this);
		setContentView(R.layout.level_menu_list_view);
		
		currentFolder = Client.client.allFolders.get(getIntent().getStringExtra(PlayMenuActivity.EXTRA_FOLDER));
		setTitle(currentFolder.name);
		
		if (Client.client.isFinishPart1())
		{
			start();
			return;
		}
		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage(getString(R.string.loading));
		dialog.setCancelable(false);
		dialog.show();
		
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					Client.client.finishPart1();
				}
				catch (InterruptedException e)
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
		}.start();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		Debug.onStart(this);
		isStartingGameInProcess = false;
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		Debug.onStop(this);
	}
	
	public void start()
	{
		String[] names;
		if (currentFolder.folders != null)
		{
			names = new String[currentFolder.folders.length];
			for (int i = 0; i < currentFolder.folders.length; i++)
				names[i] = currentFolder.folders[i].name;
		}
		else
		{
			names = new String[currentFolder.games.length];
			for (int i = 0; i < currentFolder.games.length; i++)
				names[i] = Localization.get(currentFolder.games[i].gameID + ".name");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_item, R.id.text_view, names);
		setListAdapter(adapter);
		
		int i = "campaign".equals(getIntent().getStringExtra(PlayMenuActivity.EXTRA_FOLDER)) ? 0 : 5;
		GameActivity.startGame(this, currentFolder.games[i].gameID, false);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		if (isStartingGameInProcess)
			return;
		isStartingGameInProcess = true;
		if (currentFolder.folders != null)
		{
			currentFolder = currentFolder.folders[position];
			start();
		}
		else
			GameActivity.startGame(this, currentFolder.games[position].gameID, false);
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
