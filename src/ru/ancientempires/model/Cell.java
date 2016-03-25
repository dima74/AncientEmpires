package ru.ancientempires.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Cell
{
	
	public CellType	type;
	public int		i;
	public int		j;
					
	// только для захватываемых клеточек
	// public boolean isCapture;
	public Player	player;
					
	public boolean isCapture()
	{
		return player != null;
	}
	
	// только для разрушаемых клеточек
	public boolean isDestroy;
	
	// Для редактора карт
	public Cell(CellType type)
	{
		this.type = type;
		initFromType();
	}
	
	// тоже
	public Cell(Cell cell)
	{
		this(cell.type);
		player = cell.player;
	}
	
	public Cell(CellType type, int i, int j)
	{
		this(type);
		this.i = i;
		this.j = j;
	}
	
	// Возможно пригодится для редактора карт
	public void initFromType()
	{
		isDestroy = type.isDestroyDefault;
	}
	
	public boolean needSave()
	{
		return isCapture() || isDestroy != type.isDestroyDefault;
	}
	
	public void save(DataOutputStream output, Game game) throws IOException
	{
		output.writeBoolean(isCapture());
		if (isCapture())
			output.write(player.ordinal);
		output.writeBoolean(isDestroy);
	}
	
	public void load(DataInputStream input, Game game) throws IOException
	{
		boolean isCapture = input.readBoolean();
		if (isCapture)
			player = game.players[input.read()];
		isDestroy = input.readBoolean();
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
		if (isCapture() != cell.isCapture())
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
		return type.name;
	}
	
}
