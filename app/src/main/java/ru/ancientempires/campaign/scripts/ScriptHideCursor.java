package ru.ancientempires.campaign.scripts;

public class ScriptHideCursor extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.hideCursor(this);
	}
	
}
