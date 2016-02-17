package ru.ancientempires.campaign.scripts;

public class ScriptDisableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.disableActiveGame(this);
	}
	
}
