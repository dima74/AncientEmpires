package ru.ancientempires.campaign.conditions;

public class Bounds extends AbstractBounds
{
	
	public int iMin;
	public int jMin;
	public int iMax;
	public int jMax;
	
	public Bounds()
	{
	}
	
	public Bounds(int iMin, int jMin, int iMax, int jMax)
	{
		this.iMin = iMin;
		this.jMin = jMin;
		this.iMax = iMax;
		this.jMax = jMax;
	}
	
	public boolean in(int i, int j)
	{
		return iMin <= i && i <= iMax && jMin <= j && j <= jMax;
	}

}
