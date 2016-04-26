package ru.ancientempires.reflection;

import ru.ancientempires.campaign.points.PointFrom;

public class PointOffset extends PointFrom
{

	public int offsetI;
	public int offsetJ;

	public PointOffset() {}

	public PointOffset(int offsetI, int offsetJ, Object... point)
	{
		super(point);
		this.offsetI = offsetI;
		this.offsetJ = offsetJ;
	}

	@Override
	public int getI()
	{
		return super.getI() + offsetI;
	}

	@Override
	public int getJ()
	{
		return super.getJ() + offsetJ;
	}

}
