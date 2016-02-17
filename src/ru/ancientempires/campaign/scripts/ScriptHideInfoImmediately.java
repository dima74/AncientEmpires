package ru.ancientempires.campaign.scripts;

public class ScriptHideInfoImmediately extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.hideInfoImmediately(this);
	}
	
}
