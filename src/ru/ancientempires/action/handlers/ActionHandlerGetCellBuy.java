package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Unit;

public class ActionHandlerGetCellBuy extends ActionHandler
{
	
	public ActionHandlerGetCellBuy(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerGetCellBuy(this.type);
	}
	
	@Override
	public ActionResult action(Action action)
	{
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		Cell cell = GameHandler.fieldCells[i][j];
		CellType cellType = cell.type;
		
		ArrayList<Unit> units = new ArrayList<Unit>(cellType.buyUnits[cell.player.ordinal]);
		units.addAll(0, GameHandler.game.staticUnitsDead[cell.player.ordinal]);
		boolean[] isAvailable = new boolean[units.size()];
		for (int k = 0; k < isAvailable.length; k++)
			isAvailable[k] = GameHandler.game.currentPlayer.gold >= units.get(k).cost;
			
		this.result.setProperty("units", units.toArray(new Unit[0]));
		this.result.setProperty("isAvailable", isAvailable);
		
		return this.result;
	}
	
}
