package ru.ancientempires.campaign.points;

import org.atteo.classindex.IndexSubclasses;

import java.util.ArrayList;

import ru.ancientempires.handler.IGameHandler;

@IndexSubclasses
public abstract class AbstractPoint extends IGameHandler
{
	
	public abstract int getI();
	
	public abstract int getJ();
	
	@Override
	public int hashCode()
	{
		final int prime = 777;
		int result = 1;
		result = prime * result + getI();
		result = prime * result + getJ();
		return result;
	}

	@Override
	public boolean equals(Object o)
	{
		AbstractPoint p = (AbstractPoint) o;
		return p.getI() == getI() && p.getJ() == getJ();
	}

	public static AbstractPoint[] createPoints(Object... points)
	{
		ArrayList<AbstractPoint> list = new ArrayList<>();
		for (int i = 0; i < points.length; i++)
		{
			Object o = points[i];
			if (o instanceof AbstractPoint)
				list.add((AbstractPoint) o);
			else if (o.getClass() == Integer.class)
			{
				list.add(new PointInteger((int) points[i], (int) points[i + 1]));
				++i;
			} else if (o.getClass() == String.class)
				list.add(new PointNamed((String) o));
		}
		return list.toArray(new AbstractPoint[0]);
	}

	public static AbstractPoint createPoint(Object... points)
	{
		return createPoints(points)[0];
	}

	@Override
	public String toString()
	{
		return "{" + getI() + ", " + getJ() + "}";
	}
	
}
