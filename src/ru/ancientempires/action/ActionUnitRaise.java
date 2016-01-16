package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionUnitRaise extends ActionFromTo
{
	
	private Unit	unit;
	private Unit	targetUnit;
	
	@Override
	public ActionResult perform()
	{
		if (!check(checkRaise()))
			return null;
		performQuick();
		return commit();
	}
	
	private boolean checkRaise()
	{
		if (!(game.checkCoordinates(i, j) && game.checkCoordinates(targetI, targetJ)))
			return false;
			
		unit = game.fieldUnits[i][j];
		targetUnit = game.fieldUnitsDead[targetI][targetJ];
		
		return unit != null && targetUnit != null && !unit.isTurn && ActionUnitRaise.boundsIsNorm(unit, targetUnit);
	}
	
	private static boolean boundsIsNorm(Unit unit, Unit targetUnit)
	{
		RangeType type = unit.type.raiseRange;
		boolean[][] field = type.field;
		int size = field.length;
		
		int relI = targetUnit.i - unit.i + type.radius;
		int relJ = targetUnit.j - unit.j + type.radius;
		
		return relI >= 0 && relI < size && relJ >= 0 && relJ < size && field[relI][relJ];
	}
	
	@Override
	public void performQuick()
	{
		unit.setTurn();
		
		Unit raisedUnit = new Unit(unit.type.raiseUnit, unit.player);
		raisedUnit.i = targetI;
		raisedUnit.j = targetJ;
		raisedUnit.player.units.add(raisedUnit);
		raisedUnit.isTurn = true;
		game.setUnit(targetI, targetJ, raisedUnit);
		game.fieldUnitsDead[targetI][targetJ] = null;
	}
	
}
