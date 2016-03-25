package ru.ancientempires.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import ru.ancientempires.MenuActions;
import ru.ancientempires.R;

public class PlayMenuActivity extends BaseListActivity
{
	
	public static final String		EXTRA_FOLDER	= "ru.ancientempires.folder";
													
	private static MenuActions[]	actions			= new MenuActions[]
														{
															MenuActions.CAMPAIGN,
															MenuActions.SKIRMISH,
															MenuActions.USER_MAPS,
															MenuActions.LOAD
														};
														
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, actions);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (MainActivity.gameToStart != "" && MainActivity.firstStart)
			start(MainActivity.gameToStart);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case CAMPAIGN:
				start("campaign");
				break;
			case SKIRMISH:
				start("skirmish");
				break;
			case USER_MAPS:
				start("user");
				break;
			case LOAD:
				start("save");
				break;
			default:
				break;
		}
	}
	
	private void start(String folderID)
	{
		startActivity(new Intent(this, LevelMenuActivity.class).putExtra(PlayMenuActivity.EXTRA_FOLDER, folderID));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_menu, menu);
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
		{
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
