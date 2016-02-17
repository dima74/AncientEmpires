package ru.ancientempires.campaign.scripts;

import ru.ancientempires.framework.MyAssert;

public class ScriptCloseMission extends Script
{
	
	@Override
	public void start()
	{
		if (game.path.nextGameID != null)
			try
			{
				campaign.iDrawCampaign.closeMission();
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
		else
		{
			// TODO
		}
	}
}
