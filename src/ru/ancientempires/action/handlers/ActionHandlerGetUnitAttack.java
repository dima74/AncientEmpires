package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionHandlerGetUnitAttack extends ActionHandler
{
	
	public ActionHandlerGetUnitAttack(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerGetUnitAttack(this.type);
	}
	
	@Override
	public ActionResult action(Action action)
	{
		int i = (int) action.getProperty("i");
		int j = (int) action.getProperty("j");
		
		Unit unit = GameHandler.getUnit(i, j);
		RangeType type = unit.type.attackRange;
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
				int atttackedI = unit.i + relI - type.radius;
				int atttackedJ = unit.j + relJ - type.radius;
				if (!GameHandler.checkCoord(atttackedI, atttackedJ))
					continue;
				Unit targetUnit = GameHandler.fieldUnits[atttackedI][atttackedJ];
				Cell targetCell = GameHandler.fieldCells[atttackedI][atttackedJ];
				realField[relI][relJ] = allField[relI][relJ] && (targetUnit != null && targetUnit.player != unit.player
						|| unit.type.destroyingTypes[targetCell.type.ordinal] && !targetCell.isDestroying && targetCell.getTeam() != unit.player.team);
			}
			
		this.result.setProperty("visibleField", allField);
		this.result.setProperty("realField", realField);
		
		return this.result;
	}
	
}
