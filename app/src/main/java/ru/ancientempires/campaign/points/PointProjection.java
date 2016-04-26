package ru.ancientempires.campaign.points;

public class PointProjection extends PointFrom
{

	public Projection projection;

	public PointProjection()
	{
	}

	public PointProjection(Projection projection, Object... point)
	{
		super(point);
		this.projection = projection;
	}

	@Override
	public int getI()
	{
		return game.h * projection.multiH + super.getI() * projection.multiI + projection.offsetI;
	}

	@Override
	public int getJ()
	{
		return game.w * projection.multiW + super.getJ() * projection.multiJ + projection.offsetJ;
	}

}
