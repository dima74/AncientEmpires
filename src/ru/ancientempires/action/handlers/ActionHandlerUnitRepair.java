package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Unit;

public class ActionHandlerUnitRepair extends ActionHandler
{
	
	public ActionHandlerUnitRepair(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerUnitRepair(this.type);
	}
	
	private Unit unit;
	
	@Override
	public ActionResult action(Action action)
	{
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		if (!GameHandler.checkCoord(i, j))
			this.result.successfully = false;
		else
		{
			this.unit = GameHandler.getUnit(i, j);
			boolean canRepair = checkUnitRepair() && ActionHandlerHelper.canUnitRepair(this.unit);
			if (canRepair)
			{
				Cell cell = GameHandler.fieldCells[i][j];
				cell.isDestroying = false;
				this.unit.isTurn = true;
			}
			this.result.successfully = canRepair;
		}
		return this.result;
	}
	
	private boolean checkUnitRepair()
	{
		return this.unit != null && !this.unit.isTurn && this.unit.player == GameHandler.game.currentPlayer;
	}
	
}
