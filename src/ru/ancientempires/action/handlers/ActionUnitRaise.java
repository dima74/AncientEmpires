package ru.ancientempires.action.handlers;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionUnitRaise extends ActionFromTo
{
	
	private Unit	unit;
	private Unit	targetUnit;
	
	@Override
	public ActionResult action()
	{
		result.successfully = tryRaise();
		return result;
	}
	
	private boolean tryRaise()
	{
		return checkRaise() && raise();
	}
	
	private boolean checkRaise()
	{
		if (!(GameHandler.checkCoord(i, j) && GameHandler.checkCoord(targetI, targetJ)))
			return false;
			
		unit = GameHandler.fieldUnits[i][j];
		targetUnit = GameHandler.fieldDeadUnits[targetI][targetJ];
		
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
	
	private boolean raise()
	{
		unit.setTurn();
		
		Unit raisedUnit = new Unit(unit.type.raiseUnit, unit.player);
		raisedUnit.i = targetI;
		raisedUnit.j = targetJ;
		raisedUnit.player.units.add(raisedUnit);
		raisedUnit.isTurn = true;
		GameHandler.setUnit(targetI, targetJ, raisedUnit);
		GameHandler.fieldDeadUnits[targetI][targetJ] = null;
		return true;
	}
	
}
