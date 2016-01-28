package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class ActionUnitRepair extends ActionFrom
{
	
	@Override
	public ActionResult perform(Game game)
	{
		performBase(game);
		return null;
	}
	
	@Override
	public boolean check()
	{
		return super.check() && new ActionHelper(game).canUnitRepair(i, j);
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		cell.isDestroy = false;
		unit.isTurn = true;
	}
	
}
