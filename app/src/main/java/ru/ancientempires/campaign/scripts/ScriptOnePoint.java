package ru.ancientempires.campaign.scripts;

import ru.ancientempires.campaign.points.AbstractPoint;

public abstract class ScriptOnePoint extends Script
{
	
	public AbstractPoint point;
	
	public ScriptOnePoint()
	{
	}
	
	public ScriptOnePoint(Object... points)
	{
		this.point = AbstractPoint.createPoint(points);
	}

	public int i()
	{
		return point.getI();
	}

	public int j()
	{
		return point.getJ();
	}
	
}
