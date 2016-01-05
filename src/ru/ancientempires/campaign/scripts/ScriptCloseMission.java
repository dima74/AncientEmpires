package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptCloseMission extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		if (campaign.game.path.nextGameID != null)
			campaign.iDrawCampaign.closeMission();
		else
		{
			// TODO
		}
	}
}
