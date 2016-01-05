package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptDisableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.disableActiveGame(this);
	}
	
}
