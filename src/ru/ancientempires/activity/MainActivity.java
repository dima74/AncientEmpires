package ru.ancientempires.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ru.ancientempires.MenuActions;

public class MainActivity extends BaseListActivity
{
	
	public static int				skirmish	= 5;
	public static int				campaign	= 1;
	public static String			gameToStart	= "test";
	public static boolean			firstStart	= true;
												
	private static MenuActions[]	actions		= new MenuActions[]
													{
														MenuActions.PLAY,
														MenuActions.ONLINE,
														MenuActions.SETTINGS,
														MenuActions.MAP_EDITOR,
														MenuActions.INSTRUCTIONS,
														MenuActions.AUTHORS
													};
													
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState, actions);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case PLAY:
				moveTo(PlayMenuActivity.class);
				break;
			case MAP_EDITOR:
				moveTo(EditorBaseActivity.class);
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (firstStart && "editor".equals(gameToStart))
			moveTo(EditorBaseActivity.class);
		else if (firstStart && gameToStart != "")
			moveTo(PlayMenuActivity.class);
	}
	
}
