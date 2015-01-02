package ru.ancientempires.helpers;

public class Point
{
	
	public static final Object	NULL_POINT	= new Point(-1, -1);
	
	public int					i, j;
	
	public Point(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Point p = (Point) o;
		return p.i == this.i && p.j == this.j;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 777;
		int result = 1;
		result = prime * result + this.i;
		result = prime * result + this.j;
		return result;
	}
	
	@Override
	public String toString()
	{
		return "(" + this.i + ", " + this.j + ")";
	}
	
}
