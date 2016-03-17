package ru.ancientempires.activity;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import ru.ancientempires.MenuActions;
import ru.ancientempires.client.AndroidClientHelper;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;

public class MainActivity extends BaseListActivity
{
	
	public static String			gameToStart	= "test";
	public static boolean			firstStart	= false;
												
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
		if (Client.client == null)
			try
			{
				Client.client = new Client(new AndroidClientHelper(this));
				Client.client.startLoadParts12();
			}
			catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		super.onCreate(savedInstanceState, actions);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (actions[position])
		{
			case PLAY:
				startActivity(new Intent(this, PlayMenuActivity.class));
				break;
			case MAP_EDITOR:
				startActivity(new Intent(this, MapEditorActivity.class));
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		if (gameToStart != "" && firstStart)
			startActivity(new Intent(this, PlayMenuActivity.class));
	}
	
}
