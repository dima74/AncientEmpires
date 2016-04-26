package ru.ancientempires.campaign.points;

public class PointInteger extends AbstractPoint
{
	
	public int i, j;

	public PointInteger()
	{
	}

	public PointInteger(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public int getI()
	{
		return i;
	}
	
	@Override
	public int getJ()
	{
		return j;
	}

}
