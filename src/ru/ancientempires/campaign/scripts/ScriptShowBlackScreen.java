package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptShowBlackScreen extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showBlackScreen(this);
	}
	
}
