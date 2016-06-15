package ru.ancientempires.activities;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.swing.Swing;

public class GameActivity extends BaseGameActivity
{
	
	public static GameActivity activity;
	
	public static void startGame(String gameID, boolean useLastTeams)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					if (activity != null)
						activity.view.frame.dispose();
					Thread.sleep(1000);
					new Swing(gameID);
				}
				catch (Exception e)
				{
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void postUpdateCampaign()
	{
		view.needUpdateCampaign = true;
	}
	
}
