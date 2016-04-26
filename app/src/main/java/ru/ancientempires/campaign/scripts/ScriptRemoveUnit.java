package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.ActionCampaignRemoveUnit;

public class ScriptRemoveUnit extends ScriptOnePoint
{

	public ScriptRemoveUnit()
	{
	}
	
	public ScriptRemoveUnit(Object... point)
	{
		super(point);
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.removeUnit(i(), j(), this);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignRemoveUnit()
				.setIJ(i(), j())
				.perform(game);
	}
	
}
