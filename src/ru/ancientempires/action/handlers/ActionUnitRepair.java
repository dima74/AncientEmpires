package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionUnitRepair extends Action
{
	
	public int		i;
	public int		j;
	private Unit	unit;
	
	public ActionUnitRepair()
	{}
	
	public ActionUnitRepair(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public ActionResult action()
	{
		if (!GameHandler.checkCoord(i, j))
			result.successfully = false;
		else
		{
			unit = GameHandler.getUnit(i, j);
			boolean canRepair = checkUnitRepair() && ActionHandlerHelper.canUnitRepair(unit);
			if (canRepair)
			{
				Cell cell = GameHandler.fieldCells[i][j];
				cell.isDestroying = false;
				unit.isTurn = true;
			}
			result.successfully = canRepair;
		}
		return result;
	}
	
	private boolean checkUnitRepair()
	{
		return unit != null && !unit.isTurn && unit.player == GameHandler.game.currentPlayer;
	}
	
}
