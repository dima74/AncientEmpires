package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class ActionUnitCapture extends ActionFrom
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
		return super.check() && new ActionHelper(game).canUnitCapture(i, j);
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		if (cell.player != null)
			game.currentEarns[cell.player.ordinal] -= cell.type.earn;
			
		cell.isCapture = true;
		cell.player = unit.player;
		unit.setTurn();
		game.currentEarns[unit.player.ordinal] += cell.type.earn;
	}
	
}
