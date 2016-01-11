package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Unit;

public class ActionGetCellBuy extends ActionFrom
{
	
	@Override
	public ActionResult action()
	{
		Cell cell = GameHandler.fieldCells[i][j];
		CellType cellType = cell.type;
		
		ArrayList<Unit> units = new ArrayList<Unit>(cellType.buyUnits[cell.player.ordinal]);
		units.addAll(0, GameHandler.game.unitsStaticDead[cell.player.ordinal]);
		boolean[] isAvailable = new boolean[units.size()];
		for (int k = 0; k < isAvailable.length; k++)
			isAvailable[k] = GameHandler.game.currentPlayer.gold >= units.get(k).cost;
			
		result.setProperty("units", units.toArray(new Unit[0]));
		result.setProperty("isAvailable", isAvailable);
		
		return result;
	}
	
}
