package ru.ancientempires.action.handlers;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.ii.II;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public abstract class GameHandler
{
	
	public static Game		game;
	public static Cell[][]	fieldCells;
	public static Unit[][]	fieldUnits;
	public static Unit[][]	fieldDeadUnits;
	public static int		h;
	public static int		w;
	public static int		amountPlayers;
	
	public static void initGame(Game game)
	{
		GameHandler.game = game;
		GameHandler.fieldCells = game.fieldCells;
		GameHandler.fieldUnits = game.fieldUnits;
		GameHandler.fieldDeadUnits = game.fieldDeadUnits;
		GameHandler.h = game.h;
		GameHandler.w = game.w;
		GameHandler.amountPlayers = game.players.length;
		game.currentEarns = new int[game.players.length];
		ActionHandlerHelper.calculateCacheValues();
		
		II.ii = new II();
		II.ii.init();
	}
	
	public static boolean checkCoord(int i, int j)
	{
		return i >= 0 && i < GameHandler.h && j >= 0 && j < GameHandler.w;
	}
	
	public static Unit getUnit(int i, int j)
	{
		if (GameHandler.checkCoord(i, j))
			return GameHandler.fieldUnits[i][j];
		return GameHandler.getUnitOutside(i, j);
	}
	
	public static void setUnit(int i, int j, Unit unit)
	{
		Unit floatingUnit = GameHandler.getUnit(i, j);
		if (floatingUnit != null)
		{
			MyAssert.a(GameHandler.game.floatingUnit == null);
			GameHandler.game.floatingUnit = floatingUnit;
		}
		
		unit.i = i;
		unit.j = j;
		if (GameHandler.checkCoord(i, j))
			GameHandler.fieldUnits[i][j] = unit;
		else
			GameHandler.game.unitsOutside.add(unit);
	}
	
	public static void removeUnit(int i, int j)
	{
		Unit unit = GameHandler.getUnit(i, j);
		if (GameHandler.checkCoord(i, j))
			GameHandler.fieldUnits[i][j] = null;
		else
			GameHandler.game.unitsOutside.remove(unit);
			
		Unit floatingUnit = GameHandler.game.floatingUnit;
		if (floatingUnit != null && i == floatingUnit.i && j == floatingUnit.j)
		{
			GameHandler.game.floatingUnit = null;
			GameHandler.setUnit(i, j, floatingUnit);
		}
	}
	
	private static Unit getUnitOutside(int i, int j)
	{
		for (Unit unit : GameHandler.game.unitsOutside)
			if (unit.i == i && unit.j == j)
				return unit;
		return null;
	}
	
}
