package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptHideBlackScreen extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.hideBlackScreen(this);
	}
	
}
