package ru.ancientempires.model;

public class Cell
{
	
	public static Cell		defaultCell;
	public static Cell[]	staticCells;
	
	public static void setStaticCells(CellType[] staticCellsTypes)
	{
		Cell.staticCells = new Cell[staticCellsTypes.length];
		for (int i = 0; i < staticCellsTypes.length; i++)
			Cell.staticCells[i] = new Cell(Cell.defaultCell, staticCellsTypes[i]);
	}
	
	public static Cell getNew(CellType cellType)
	{
		if (cellType.isStatic)
			return Cell.staticCells[cellType.staticOrdinal];
		else
			return new Cell(Cell.defaultCell, cellType);
	}
	
	public CellType	type;
	public int		i;
	public int		j;
	
	// только для захватываемых клеточек
	public boolean	isCapture;
	public Player	player;
	
	// только для разрушаемых клеточек
	public boolean isDestroy;
	
	public Cell(Cell cell, CellType type)
	{
		this.type = type;
		if (cell == null)
			return;
		isCapture = cell.isCapture;
		isDestroy = cell.isDestroy;
		i = cell.i;
		j = cell.j;
	}
	
	public int getSteps()
	{
		return type.steps;
	}
	
	public Team getTeam()
	{
		return player == null ? null : player.team;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Cell cell = (Cell) o;
		if (this == cell)
			return true;
		if (type != cell.type)
			return false;
		if (i != cell.i)
			return false;
		if (j != cell.j)
			return false;
		if (isCapture != cell.isCapture)
			return false;
		if (isDestroy != cell.isDestroy)
			return false;
		if (player == null)
		{
			if (cell.player != null)
				return false;
		}
		else if (player.ordinal != cell.player.ordinal)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Cell [type=" + type + "]";
	}
	
}
