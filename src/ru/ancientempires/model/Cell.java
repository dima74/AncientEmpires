package ru.ancientempires.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.handler.IGameHandler;

public class Cell extends IGameHandler
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
	
	// Для редактора карт
	public Cell(Game game, CellType type)
	{
		setGame(game);
		this.type = type;
	}
	
	// тоже
	public Cell(Cell cell)
	{
		this(cell.game, cell.type);
		player = cell.player;
	}
	
	// в принципе можно прямо тут обновлять fieldCell
	public Cell(Game game, CellType type, int i, int j)
	{
		this(game, type);
		this.i = i;
		this.j = j;
	}
	
	public void destroy()
	{
		game.fieldCells[i][j] = new Cell(game, type.destroyingType, i, j);
	}
	
	public void repair()
	{
		game.fieldCells[i][j] = new Cell(game, type.repairType, i, j);
	}
	
	public boolean needSave()
	{
		return isCapture();
	}
	
	public void save(DataOutputStream output, Game game) throws IOException
	{
		output.writeBoolean(isCapture());
		if (isCapture())
			output.write(player.ordinal);
	}
	
	public void load(DataInputStream input, Game game) throws IOException
	{
		boolean isCapture = input.readBoolean();
		if (isCapture)
			player = game.players[input.read()];
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
