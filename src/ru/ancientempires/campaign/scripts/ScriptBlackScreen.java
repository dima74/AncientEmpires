package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptBlackScreen extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.blackScreen(this);
	}
	
}
