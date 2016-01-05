package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionHandlerUnitRaise extends ActionHandler
{
	
	public ActionHandlerUnitRaise(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerUnitRaise(this.type);
	}
	
	private int	i;
	private int	j;
	private int	targetI;
	private int	targetJ;
	
	private Unit	unit;
	private Unit	targetUnit;
	
	@Override
	public ActionResult action(Action action)
	{
		this.i = (int) action.getProperty("i");
		this.j = (int) action.getProperty("j");
		this.targetI = (int) action.getProperty("targetI");
		this.targetJ = (int) action.getProperty("targetJ");
		
		this.result.successfully = tryRaise();
		return this.result;
	}
	
	private boolean tryRaise()
	{
		return checkRaise() && raise();
	}
	
	private boolean checkRaise()
	{
		if (!(GameHandler.checkCoord(this.i, this.j) && GameHandler.checkCoord(this.targetI, this.targetJ)))
			return false;
			
		this.unit = GameHandler.fieldUnits[this.i][this.j];
		this.targetUnit = GameHandler.fieldDeadUnits[this.targetI][this.targetJ];
		
		return this.unit != null && this.targetUnit != null && !this.unit.isTurn && ActionHandlerUnitRaise.boundsIsNorm(this.unit, this.targetUnit);
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
		this.unit.setTurn();
		
		Unit raisedUnit = new Unit(this.unit.type.raiseUnit, this.unit.player);
		raisedUnit.i = this.targetI;
		raisedUnit.j = this.targetJ;
		raisedUnit.player.units.add(raisedUnit);
		raisedUnit.isTurn = true;
		GameHandler.setUnit(this.targetI, this.targetJ, raisedUnit);
		GameHandler.fieldDeadUnits[this.targetI][this.targetJ] = null;
		return true;
	}
	
}
