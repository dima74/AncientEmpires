package ru.ancientempires.action;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultGetCellBuy;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Unit;

public class ActionGetCellBuy extends ActionFrom
{
	
	@Override
	public boolean changesGame()
	{
		return false;
	}
	
	@Override
	public ActionResultGetCellBuy perform()
	{
		Cell cell = game.fieldCells[i][j];
		CellType cellType = cell.type;
		
		ArrayList<Unit> units = new ArrayList<Unit>(cellType.buyUnits[cell.player.ordinal]);
		units.addAll(0, game.unitsStaticDead[cell.player.ordinal]);
		boolean[] isAvailable = new boolean[units.size()];
		for (int k = 0; k < isAvailable.length; k++)
			isAvailable[k] = game.currentPlayer.gold >= units.get(k).cost;
			
		return commit(new ActionResultGetCellBuy(units, isAvailable));
	}
	
}
