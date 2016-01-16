package ru.ancientempires.campaign.scripts;

public class ScriptShowCursor extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showCursor(this);
	}
	
}
