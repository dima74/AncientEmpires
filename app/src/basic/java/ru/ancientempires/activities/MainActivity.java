package ru.ancientempires.activities;

import android.view.View;
import android.widget.ListView;

import ru.ancientempires.MenuActions;

public class MainActivity extends BaseListActivity
{

	public static int     skirmish    = 5;
	public static int     campaign    = 0;
	public static String  gameToStart = "campaign";
	public static boolean firstStart  = true;

	@Override
	public MenuActions[] getStrings()
	{
		return new MenuActions[] {
				MenuActions.PLAY,
				MenuActions.ONLINE,
				MenuActions.SETTINGS,
				MenuActions.MAP_EDITOR,
				MenuActions.INSTRUCTIONS,
				MenuActions.AUTHORS
		};
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		switch (getStrings()[position])
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
		if (firstStart)
		{
			if ("info".equals(gameToStart))
				startApplicationDetailsActivity(this);
			else if ("editor".equals(gameToStart))
				moveTo(EditorBaseActivity.class);
			else if ("TestActivity".equals(gameToStart))
				moveTo(TestActivity.class);
			else if (gameToStart != "")
				moveTo(PlayMenuActivity.class);
		}
	}

	@Override
	public void onBackPressed()
	{
		finish();
	}

}
