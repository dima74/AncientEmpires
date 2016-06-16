package ru.ancientempires.activities;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.swing.Swing;

public class GameActivity extends BaseGameActivity
{
	
	public static GameActivity activity;
	
	public static void startGame(String gameID, String lastTeams)
	{
		new Thread(() -> {
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
		}).start();
	}
	
	public void postUpdateCampaign()
	{
		view.needUpdateCampaign = true;
	}
	
}