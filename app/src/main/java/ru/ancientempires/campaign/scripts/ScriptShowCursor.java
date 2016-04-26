package ru.ancientempires.campaign.scripts;

public class ScriptShowCursor extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.showCursor(this);
	}
	
}
