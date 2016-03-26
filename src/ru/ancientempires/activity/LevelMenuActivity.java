package ru.ancientempires.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.Extras;
import ru.ancientempires.MyAsyncTask;
import ru.ancientempires.R;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;

public class LevelMenuActivity extends BaseListActivity
{
	
	public GamesFolder	currentFolder;
	private boolean		isStartingGameInProcess;
	private String		folderID;
						
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_menu_list_view);
		
		folderID = getIntent().getStringExtra(Extras.FOLDER_ID);
		MyAssert.a(folderID != null);
		
		if (Client.client.isFinishPart1())
		{
			showFolders();
			return;
		}
		
		new MyAsyncTask(this)
		{
			@Override
			public void doInBackground() throws Exception
			{
				Client.client.finishPart1();
			};
			
			@Override
			public void onPostExecute()
			{
				showFolders();
			}
		}.start();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		isStartingGameInProcess = false;
	}
	
	public void showFolders()
	{
		currentFolder = Client.client.allFolders.get(folderID);
		MyAssert.a(currentFolder.name != null);
		setTitle(currentFolder.name);
		
		ArrayList<String> names = new ArrayList<>();
		for (GamePath game : currentFolder.games)
			names.add(currentFolder.getName(names.size(), game));
		for (String name : names)
			MyAssert.a(name != null);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_item, R.id.text_view, names);
		setListAdapter(adapter);
		
		String focus = getIntent().getStringExtra(Extras.FOCUS_ON);
		if (focus != null)
			for (int i = 0; i < currentFolder.games.size(); i++)
				if (focus.equals(currentFolder.games.get(i).gameID))
				{
					setSelection(i);
					break;
				}
				
		if (MainActivity.gameToStart != "" && MainActivity.firstStart)
		{
			// String folder = getIntent().getStringExtra(PlayMenuActivity.EXTRA_FOLDER);
			int i = "campaign".equals(folderID) ? 0 : "skirmish".equals(folderID) ? 5 : 0;
			onListItemClick(null, null, i, 0);
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		if (isStartingGameInProcess)
			return;
		isStartingGameInProcess = true;
		GameActivity.startGame(this, currentFolder.games.get(position).gameID, false);
		finish();
	}
	
	@Override
	public void onBackPressed()
	{
		moveTo(PlayMenuActivity.class);
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
