package ru.ancientempires.campaign.points;

import ru.ancientempires.action.ActionCampaignSetNamedPoint;
import ru.ancientempires.campaign.scripts.Script;

public class ScriptSetNamedPoint extends Script
{

	public String        name;
	public AbstractPoint point;

	public ScriptSetNamedPoint()
	{}

	public ScriptSetNamedPoint(String name, AbstractPoint point)
	{
		this.name = name;
		this.point = point;
	}

	@Override
	public void start()
	{
		new ActionCampaignSetNamedPoint(name, point)
				.perform(game);
	}

}
