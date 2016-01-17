package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionUnitRepair extends ActionFrom
{
	
	private Unit unit;
	
	@Override
	public ActionResult perform()
	{
		if (!check(game.checkCoordinates(i, j) && new ActionHelper(game).canUnitRepair(i, j)))
			return null;
		performQuick();
		return commit();
	}
	
	@Override
	public void performQuick()
	{
		unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		cell.isDestroying = false;
		unit.isTurn = true;
	}
	
}
