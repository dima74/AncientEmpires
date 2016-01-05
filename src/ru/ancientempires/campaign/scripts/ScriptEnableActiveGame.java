package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptEnableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.enableActiveGame(this);
	}
	
}
