package ru.ancientempires.campaign.scripts;

public class ScriptEnableActiveGame extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.enableActiveGame(this);
	}
	
	@Override
	public void performAction()
	{
		campaign.needSaveSnapshot = true;
	}
	
}
