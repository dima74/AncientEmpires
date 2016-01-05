package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.Campaign;

public class ScriptGameOver extends Script
{
	
	@Override
	public void start()
	{
		super.start();
		campaign.iDrawCampaign.gameOver(this);
	}
	
}
