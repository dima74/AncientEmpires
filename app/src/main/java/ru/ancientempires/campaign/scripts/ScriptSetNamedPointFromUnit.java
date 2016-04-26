package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.ActionCampaignSetNamedPoint;
import ru.ancientempires.campaign.points.PointInteger;
import ru.ancientempires.model.Unit;

public class ScriptSetNamedPointFromUnit extends Script
{

	public String pointName;
	public String unitName;

	public ScriptSetNamedPointFromUnit()
	{
	}

	public ScriptSetNamedPointFromUnit(String pointName, String unitName)
	{
		this.pointName = pointName;
		this.unitName = unitName;
	}

	@Override
	public void start()
	{
		Unit unit = game.namedUnits.get(unitName);
		new ActionCampaignSetNamedPoint(pointName, new PointInteger(unit.i, unit.j))
				.perform(game);
	}

}
