package ru.ancientempires.action;

import java.util.ArrayList;
import java.util.Arrays;

import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class ActionGetCellBuy extends ActionFrom
{
	
	private ActionResultGetCellBuy result;
	
	@Override
	public boolean changesGame()
	{
		return false;
	}
	
	@Override
	public ActionResultGetCellBuy perform(Game game)
	{
		performBase(game);
		return result;
	}
	
	@Override
	public void performQuick()
	{
		Cell cell = game.fieldCells[i][j];
		CellType cellType = cell.type;
		
		ArrayList<Unit> units = new ArrayList<Unit>(Arrays.asList(cellType.buyUnits[cell.player.ordinal]));
		units.addAll(0, game.unitsStaticDead[cell.player.ordinal]);
		boolean[] isAvailable = new boolean[units.size()];
		for (int k = 0; k < isAvailable.length; k++)
			isAvailable[k] = cell.player.numberUnits() < game.unitsLimit
					&& game.currentPlayer.gold >= units.get(k).getCost()
					&& new ActionHelper(game).isEmptyCells(i, j, units.get(k));
					
		result = new ActionResultGetCellBuy(units, isAvailable);
	}
	
}
