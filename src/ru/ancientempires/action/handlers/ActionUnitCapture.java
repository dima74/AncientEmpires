package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionUnitCapture extends Action
{
	
	public int	i;
	public int	j;
	
	public ActionUnitCapture()
	{}
	
	public ActionUnitCapture(int i, int j)
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
			result.successfully = canCapture;
		}
		return result;
	}
}
