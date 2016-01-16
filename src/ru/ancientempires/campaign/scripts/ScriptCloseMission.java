package ru.ancientempires.campaign.scripts;

import ru.ancientempires.framework.MyAssert;

public class ScriptCloseMission extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		if (campaign.game.path.nextGameID != null)
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
