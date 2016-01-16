package ru.ancientempires.action.campaign;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.model.Unit;

public class ActionCampaignRemoveUnit extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	@Override
	public ActionResult perform()
	{
		performQuick();
		return commit();
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		game.removeUnit(i, j);
		unit.player.units.remove(unit);
	}
	
}
