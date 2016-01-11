package ru.ancientempires.campaign.scripts;

public class ScriptEnableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.enableActiveGame(this);
	}
	
}
