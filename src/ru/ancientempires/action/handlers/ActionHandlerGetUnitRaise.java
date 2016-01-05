package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionHandlerGetUnitRaise extends ActionHandler
{
	
	public ActionHandlerGetUnitRaise(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerGetUnitRaise(this.type);
	}
	
	@Override
	public ActionResult action(Action action)
	{
		// TODO объединить с GetUnitAttack (Екстендить от одного и сделать класс для проверки ***)
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		Unit unit = GameHandler.getUnit(i, j);
		RangeType type = unit.type.raiseRange;
		int size = type.radius * 2 + 1;
		
		boolean[][] allField = new boolean[size][size];
		boolean[][] realField = new boolean[size][size];
		
		int startRelI = Math.max(type.radius - unit.i, 0);
		int startRelJ = Math.max(type.radius - unit.j, 0);
		int endRelI = Math.min(GameHandler.h - startRelI, size);
		int endRelJ = Math.min(GameHandler.w - startRelJ, size);
		for (int relI = startRelI; relI < endRelI; relI++)
			for (int relJ = startRelJ; relJ < endRelJ; relJ++)
			{
				allField[relI][relJ] = type.field[relI][relJ];
				int targetI = unit.i + relI - type.radius;
				int targetJ = unit.j + relJ - type.radius;
				// ***
				realField[relI][relJ] = allField[relI][relJ] && GameHandler.fieldDeadUnits[targetI][targetJ] != null;
				// ***
			}
			
		this.result.setProperty("visibleField", allField);
		this.result.setProperty("realField", realField);
		
		return this.result;
	}
	
}
