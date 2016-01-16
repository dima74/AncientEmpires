package ru.ancientempires.handler;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public abstract class GameHandler extends IGameHandler
{
	
	public Cell[][]	fieldCells		= game.fieldCells;
	public Unit[][]	fieldUnits		= game.fieldUnits;
	public Unit[][]	fieldUnitsDead	= game.fieldUnitsDead;
	public int		h				= game.h;
	public int		w				= game.w;
	public int		numberPlayers	= game.players.length;
	
	@Override
	public void setGame(Game game)
	{
		super.setGame(game);
		fieldCells = game.fieldCells;
		fieldUnits = game.fieldUnits;
		fieldUnitsDead = game.fieldUnitsDead;
		h = game.h;
		w = game.w;
		numberPlayers = game.players.length;
	}
	
}
