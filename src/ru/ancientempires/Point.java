package ru.ancientempires;

public class Point
{
	
	public static final Point NULL_POINT = new Point(-1, -1);
	
	public int i, j;
	
	public Point(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	public Point(float i, float j)
	{
		this(Math.round(i), Math.round(j));
	}
	
	@Override
	public boolean equals(Object o)
	{
		Point p = (Point) o;
		return p.i == i && p.j == j;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 777;
		int result = 1;
		result = prime * result + i;
		result = prime * result + j;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "{" + i + ", " + j + "}";
	}
	
}
