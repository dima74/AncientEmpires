package ru.ancientempires.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ru.ancientempires.MenuActions;
import ru.ancientempires.R;

public class PlayMenuActivity extends ListActivity
{
	
	public static final String EXTRA_FOLDER = "ru.ancientempires.folder";
	
	private MenuActions[] actions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		actions = new MenuActions[]
		{
				MenuActions.CAMPAIGN,
				MenuActions.SKIRMISH,
				MenuActions.USER_MAPS,
				MenuActions.LOAD
		};
		
		setContentView(R.layout.main_menu_list_view);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.main_menu_list_item, R.id.text_view, MenuActions.convertToNames(actions));
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case CAMPAIGN:
				startActivity(new Intent(this, LevelMenuActivity.class).putExtra(PlayMenuActivity.EXTRA_FOLDER, "campaign"));
				break;
			case SKIRMISH:
				startActivity(new Intent(this, LevelMenuActivity.class).putExtra(PlayMenuActivity.EXTRA_FOLDER, "multiplayer"));
				break;
			default:
				break;
		}
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
			return true;
		return super.onOptionsItemSelected(item);
	}
	
}
