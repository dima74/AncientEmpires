package ru.ancientempires.campaign.points;

public class PointNamed extends AbstractPoint
{

	public String name;

	public PointNamed()
	{
	}

	public PointNamed(String name)
	{
		this.name = name;
	}

	@Override
	public int getI()
	{
		return game.namedPoints.get(name).getI();
	}

	@Override
	public int getJ()
	{
		return game.namedPoints.get(name).getJ();
	}
}
