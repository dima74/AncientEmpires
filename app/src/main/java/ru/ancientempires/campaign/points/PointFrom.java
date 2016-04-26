package ru.ancientempires.campaign.points;

public class PointFrom extends AbstractPoint
{

	public AbstractPoint point;

	public PointFrom() {}

	public PointFrom(Object... point)
	{
		this.point = createPoint(point);
	}

	@Override
	public int getI()
	{
		return point.getI();
	}

	@Override
	public int getJ()
	{
		return point.getJ();
	}
}
