package ru.ancientempires.campaign.scripts;

public class ScriptHideBlackScreen extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.hideBlackScreen(this);
	}
	
}
