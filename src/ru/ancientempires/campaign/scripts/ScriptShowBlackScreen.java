package ru.ancientempires.campaign.scripts;

public class ScriptShowBlackScreen extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.showBlackScreen(this);
	}
	
}
