package ru.ancientempires.campaign.scripts;

public class ScriptGameOver extends Script
{
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.gameOver(this);
	}
	
}
