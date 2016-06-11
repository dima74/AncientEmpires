package ru.ancientempires.handler;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public abstract class GameHandler extends IGameHandler
{
	
	public Cell[][] fieldCells;
	public Unit[][] fieldUnits;
	public Unit[][] fieldUnitsDead;
	public int      h;
	public int      w;
	public int      numberPlayers;
	
	@Override
	public void setGame(Game game)
	{
		super.setGame(game);
		if (game == null)
			return;
		fieldCells = game.fieldCells;
		fieldUnits = game.fieldUnits;
		fieldUnitsDead = game.fieldUnitsDead;
		h = game.h;
		w = game.w;
		numberPlayers = game.players.length;
	}
	
}
