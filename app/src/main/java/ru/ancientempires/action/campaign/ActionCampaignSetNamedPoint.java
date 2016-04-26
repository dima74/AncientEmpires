package ru.ancientempires.action.campaign;

import ru.ancientempires.action.Action;
import ru.ancientempires.campaign.points.AbstractPoint;

public class ActionCampaignSetNamedPoint extends Action
{

	@Override
	public boolean isCampaign()
	{
		return true;
	}

	public String        name;
	public AbstractPoint point;

	public ActionCampaignSetNamedPoint()
	{
	}

	public ActionCampaignSetNamedPoint(String name, AbstractPoint point)
	{
		this.name = name;
		this.point = point;
	}

	@Override
	public void performQuick()
	{
		game.namedPoints.set(name, point);
	}

}
