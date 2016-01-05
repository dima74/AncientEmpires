package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptShowCursor extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showCursor(this);
	}
	
}
