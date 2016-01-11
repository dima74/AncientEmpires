package ru.ancientempires.action.handlers;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionGetUnitRaise extends ActionFrom
{
	
	@Override
	public ActionResult action()
	{
		// TODO объединить с GetUnitAttack (Екстендить от одного и сделать класс для проверки ***)
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
			
		result.setProperty("visibleField", allField);
		result.setProperty("realField", realField);
		
		return result;
	}
	
}
