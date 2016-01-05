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
	public boolean isDestroying;
	
	public Cell(Cell cell, CellType type)
	{
		this.type = type;
		if (cell == null)
			return;
		this.isCapture = cell.isCapture;
		this.isDestroying = cell.isDestroying;
		this.i = cell.i;
		this.j = cell.j;
	}
	
	public int getSteps()
	{
		return this.type.baseSteps;
	}
	
	public Team getTeam()
	{
		return this.player == null ? null : this.player.team;
	}
	
	@Override
	public String toString()
	{
		return "Cell [type=" + this.type + "]";
	}
	
}
