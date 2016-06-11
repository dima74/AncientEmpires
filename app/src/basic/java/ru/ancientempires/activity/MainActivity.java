package ru.ancientempires.activity;

import android.view.View;
import android.widget.ListView;

import ru.ancientempires.MenuActions;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.serializable.ReflectionHelper;

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
		for (Class<?> c : ReflectionHelper.getSubclasses(Script.class))
			MyLog.l(c.getSimpleName());
		if (firstStart && "editor".equals(gameToStart))
			moveTo(EditorBaseActivity.class);
		else if (firstStart && "test".equals(gameToStart))
			moveTo(TestActivity.class);
		else if (firstStart && gameToStart != "")
			moveTo(PlayMenuActivity.class);
	}

	@Override
	public void onBackPressed()
	{
		finish();
	}

}
