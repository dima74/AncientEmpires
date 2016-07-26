package ru.ancientempires.campaign.scripts;

public class ScriptFocusOnCurrentPlayerCenter extends Script
{

	public ScriptFocusOnCurrentPlayerCenter()
	{}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.focusOnCurrentPlayerCenter();
	}

}
