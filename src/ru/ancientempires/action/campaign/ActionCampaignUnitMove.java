package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFromTo;
import ru.ancientempires.model.Unit;

public class ActionCampaignUnitMove extends ActionFromTo
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		game.removeUnit(i, j);
		game.setUnit(targetI, targetJ, unit);
	}
	
}
