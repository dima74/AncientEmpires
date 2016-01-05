package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptHideCursor extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.hideCursor(this);
	}
	
}
