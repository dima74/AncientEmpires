package ru.ancientempires.model;

public class Range
{

	public String      name;
	public int         radius;
	public int         size;
	public boolean[][] table;

	public Range(String name, boolean[][] table)
	{
		this.name = name;
		size = table.length;
		radius = size / 2;
		this.table = table;
	}

	public Range(String name, int minRadius, int maxRadius)
	{
		this.name = name;
		radius = maxRadius;
		size = radius * 2 + 1;
		table = new boolean[size][size];
		for (int i = -radius; i <= radius; i++)
			for (int j = -radius; j <= radius; j++)
			{
				int d = Math.abs(i) + Math.abs(j);
				table[radius + i][radius + j] = minRadius <= d && d <= maxRadius;
			}
	}

	public boolean checkAccess(Unit unit, Unit targetUnit)
	{
		return checkAccess(unit, targetUnit.i, targetUnit.j);
	}

	public boolean checkAccess(Unit unit, int targetI, int targetJ)
	{
		int relI = targetI - unit.i + radius;
		int relJ = targetJ - unit.j + radius;
		return 0 <= relI && relI < size && 0 <= relJ && relJ < size && table[relI][relJ];
	}

	@Override
	public String toString()
	{
		String s = "";
		for (boolean[] element : table)
		{
			for (boolean element2 : element)
				s += element2 ? '1' : '0';
			s += "\n";
		}
		return "RangeType [" + name + "]\n" + s;
	}

}
