package ru.ancientempires.action.handlers;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;

public abstract class ActionHandler extends GameHandler implements IActionHandler
{
	
	public static ActionHandler[] actionHandlers = new ActionHandler[ActionType.amount];
	
	public static ActionHandler getActionHandler(ActionType type)
	{
		ActionHandler actionHandler = ActionHandler.actionHandlers[type.ordinal].newInstance();
		actionHandler.result = new ActionResult();
		return actionHandler;
	}
	
	public static void init()
	{
		ActionHandler[] actionHandlers = new ActionHandler[]
		{
				new ActionHandlerGetUnit(ActionType.GET_UNIT),
				new ActionHandlerGetCellBuy(ActionType.GET_BUY),
				// new ActionHandlerGetActions(ActionType.GET_ACTIONS),
				// new ActionHandlerGetUnitWay(ActionType.GET_UNIT_WAY),
				// new ActionHandlerGetUnitAttack(ActionType.GET_UNIT_ATTACK),
				// new ActionHandlerGetUnitRaise(ActionType.GET_UNIT_RAISE),
				
				new ActionHandlerCellBuy(ActionType.ACTION_CELL_BUY),
				new ActionHandlerUnitMove(ActionType.ACTION_UNIT_MOVE),
				new ActionHandlerUnitRepair(ActionType.ACTION_UNIT_REPAIR),
				new ActionHandlerUnitCapture(ActionType.ACTION_UNIT_CAPTURE),
				new ActionHandlerUnitAttack(ActionType.ACTION_UNIT_ATTACK),
				new ActionHandlerUnitRaise(ActionType.ACTION_UNIT_RAISE),
				new ActionHandlerGameEndTurn(ActionType.ACTION_END_TURN)
		};
		for (ActionHandler actionHandler : actionHandlers)
			ActionHandler.actionHandlers[actionHandler.type.ordinal] = actionHandler;
	}
	
	public ActionType	type;
	public ActionResult	result;
	
	public ActionHandler(ActionType type)
	{
		this.type = type;
	}
	
}
