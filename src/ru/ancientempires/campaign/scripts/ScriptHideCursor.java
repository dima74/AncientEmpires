package ru.ancientempires.campaign.scripts;

public class ScriptHideCursor extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.hideCursor(this);
	}
	
}
