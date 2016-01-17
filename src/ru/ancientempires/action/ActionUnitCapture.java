package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionUnitCapture extends ActionFrom
{
	
	private Unit unit;
	
	@Override
	public ActionResult perform()
	{
		if (!check(game.checkCoordinates(i, j) && new ActionHelper(game).canUnitCapture(i, j)))
			return null;
			
		performQuick();
		return commit();
	}
	
	@Override
	public void performQuick()
	{
		unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		if (cell.player != null)
			game.currentEarns[cell.player.ordinal] -= cell.type.earn;
			
		cell.isCapture = true;
		cell.player = unit.player;
		unit.isTurn = true;
		game.currentEarns[unit.player.ordinal] += cell.type.earn;
	}
	
}
