package ru.ancientempires.campaign.scripts;

import ru.ancientempires.action.campaign.ActionCampaignUnitMove;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.reflection.NumberedArray;

public class ScriptUnitMove extends Script
{
	
	public AbstractPoint[]         points;
	@NumberedArray
	public ScriptUnitMoveHandler[] handlers;
	public boolean makeSmoke = true;

	public ScriptUnitMove()
	{
	}
	
	public ScriptUnitMove(Object... points)
	{
		this.points = AbstractPoint.createPoints(points);
	}

	public ScriptUnitMove setHandlers(ScriptUnitMoveHandler... handlers)
	{
		this.handlers = handlers;
		return this;
	}

	public ScriptUnitMove disableMakeSmoke()
	{
		makeSmoke = false;
		return this;
	}
	
	private AbstractPoint first()
	{
		return points[0];
	}
	
	private AbstractPoint last()
	{
		return points[points.length - 1];
	}
	
	public int i()
	{
		return first().getI();
	}
	
	public int j()
	{
		return first().getJ();
	}

	public int targetI()
	{
		return last().getI();
	}
	
	public int targetJ()
	{
		return last().getJ();
	}
	
	@Override
	public void start()
	{
		System.out.printf("%15s %5s %5s\n", game.getUnit(i(), j()).type.name, first(), last());
		campaign.iDrawCampaign.unitMove(this, true);
	}
	
	@Override
	public void performAction()
	{
		new ActionCampaignUnitMove().setIJ(i(), j()).setTargetIJ(targetI(), targetJ()).perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}
	
}
