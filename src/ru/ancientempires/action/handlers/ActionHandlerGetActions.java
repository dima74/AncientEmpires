package ru.ancientempires.action.handlers;

import java.util.ArrayList;
import java.util.Arrays;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.model.Unit;

public class ActionHandlerGetActions extends ActionHandler
{
	
	public ActionHandlerGetActions(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerGetActions(this.type);
	}
	
	@Override
	public ActionResult action(Action action)
	{
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		this.result.setProperty("actions", getActions(i, j).toArray(new ActionType[0]));
		return this.result;
	}
	
	private ArrayList<ActionType> getActions(int i, int j)
	{
		Unit unit = GameHandler.getUnit(i, j);
		MyLog.l("ga", unit);
		Unit floatingUnit = GameHandler.game.floatingUnit;
		if (floatingUnit != null)
			if (unit.i == floatingUnit.i && unit.j == floatingUnit.j)
				return new ArrayList<ActionType>(Arrays.asList(ActionType.ACTION_UNIT_MOVE));
			else
				return new ArrayList<ActionType>();
				
		ArrayList<ActionType> actionTypes = ActionHandlerHelper.getAvailableActionsForUnit(unit);
		actionTypes.addAll(getAvailableActionsForCell(i, j));
		actionTypes.add(ActionType.ACTION_END_TURN);
		return actionTypes;
	}
	
	private ArrayList<ActionType> getAvailableActionsForCell(int i, int j)
	{
		ArrayList<ActionType> actionTypes = new ArrayList<ActionType>();
		if (ActionHandlerHelper.canBuyOnCell(i, j))
			actionTypes.add(ActionType.ACTION_CELL_BUY);
		return actionTypes;
	}
	
}
