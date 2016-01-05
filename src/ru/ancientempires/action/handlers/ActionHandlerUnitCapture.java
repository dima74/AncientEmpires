package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionHandlerUnitCapture extends ActionHandler
{
	
	public ActionHandlerUnitCapture(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerUnitCapture(this.type);
	}
	
	@Override
	public ActionResult action(Action action)
	{
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		if (!GameHandler.checkCoord(i, j))
			this.result.successfully = false;
		else
		{
			Unit unit = GameHandler.getUnit(i, j);
			boolean canCapture = ActionHandlerHelper.canUnitCapture(unit);
			if (canCapture)
			{
				Cell cell = GameHandler.fieldCells[i][j];
				if (cell.player != null)
					GameHandler.game.currentEarns[cell.player.ordinal] -= cell.type.earn;
					
				cell.isCapture = true;
				cell.player = unit.player;
				unit.isTurn = true;
				GameHandler.game.currentEarns[unit.player.ordinal] += cell.type.earn;
			}
			this.result.successfully = canCapture;
		}
		return this.result;
	}
}
