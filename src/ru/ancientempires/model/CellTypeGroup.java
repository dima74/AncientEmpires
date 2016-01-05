package ru.ancientempires.model;

import java.util.ArrayList;

public class CellTypeGroup
{
	
	private static CellTypeGroup[]	groups;
	
	public static void setCellTypeGroups(ArrayList<CellTypeGroup> groups)
	{
		CellTypeGroup.number = groups.size();
		CellTypeGroup.groups = groups.toArray(new CellTypeGroup[0]);
		for (int i = 0; i < CellTypeGroup.groups.length; i++)
			CellTypeGroup.groups[i].ordinal = i;
	}
	
	public static CellTypeGroup getType(String name)
	{
		for (CellTypeGroup typeGroup : CellTypeGroup.groups)
			if (typeGroup.name.equals(name))
				return typeGroup;
		return null;
	}
	
	public static int	number;
	
	private static int getIFromNeighbours(boolean[] neighbours)
	{
		int i = 0;
		for (int j = 0; j < neighbours.length; j++)
			if (neighbours[j])
				i += 1 << j;
		return i;
	}
	
	public int		ordinal;
	
	public String	name;
	public boolean	isAdvanced;
	
	public CellTypeGroup(String name)
	{
		this.name = name.intern();
	}
	
	public CellTypeGroup(String name, boolean isAdvanced)
	{
		this.name = name.intern();
		this.isAdvanced = isAdvanced;
	}
	
	public CellType getType(boolean[] neighbours)
	{
		int i = CellTypeGroup.getIFromNeighbours(neighbours);
		if (this.cellTypes[i] == null)
			System.out.print("");
		return this.cellTypes[i];
	}
	
	private CellType[]	cellTypes	= new CellType[1 << 8];
	
	public void setType(boolean[] neighbours, CellType type)
	{
		int i = CellTypeGroup.getIFromNeighbours(neighbours);
		this.cellTypes[i] = type;
		
		int[] diagonals = new int[]
		{
				0b00000001,
				0b00000100,
				0b00100000,
				0b10000000
		};
		
		for (int j = 0; j < 1 << diagonals.length; j++)
		{
			int newI = i;
			for (int k = 0; k < diagonals.length; k++)
			{
				int di = diagonals[k];
				int dj = 1 << k;
				if ((j & dj) == dj)
					newI |= di;
			}
			this.cellTypes[newI] = type;
		}
	}
	
}
