package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.Task;
import ru.ancientempires.tasks.TaskType;
import ru.ancientempires.tasks.handlers.TaskHandler;

public class ActionHandlerUnitMove extends ActionHandler
{
	
	public ActionHandlerUnitMove(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerUnitMove(this.type);
	}
	
	private int		i;
	private int		j;
	private int		targetI;
	private int		targetJ;
	private Unit	unit;
	
	@Override
	public ActionResult action(Action action)
	{
		this.i = (int) action.getProperty("i");
		this.j = (int) action.getProperty("j");
		this.targetI = (int) action.getProperty("targetI");
		this.targetJ = (int) action.getProperty("targetJ");
		
		this.result.successfully = tryMoveUnit();
		
		return this.result;
	}
	
	private boolean tryMoveUnit()
	{
		return checkMoveUnit() && moveUnit();
	}
	
	private boolean checkMoveUnit()
	{
		if (!(GameHandler.checkCoord(this.i, this.j) && GameHandler.checkCoord(this.targetI, this.targetJ)))
			return false;
		this.unit = GameHandler.fieldUnits[this.i][this.j];
		Unit targetUnit = GameHandler.fieldUnits[this.targetI][this.targetJ];
		return this.unit != null && (targetUnit == null || targetUnit == this.unit) && canMoveUnit();
	}
	
	private boolean canMoveUnit()
	{
		return !this.unit.isMove;
	}
	
	private boolean moveUnit()
	{
		UnitType type = this.unit.type;
		
		GameHandler.fieldDeadUnits[this.targetI][this.targetJ] = null;
		GameHandler.removeUnit(this.i, this.j);
		GameHandler.setUnit(this.targetI, this.targetJ, this.unit);
		
		this.unit.isMove = true;
		this.unit.isTurn = ActionHandlerHelper.getAvailableActionsForUnit(this.unit).isEmpty();
		if (!type.canDoTwoActionAfterOne && !(this.i == this.targetI && this.j == this.targetJ))
			this.unit.setTurn();
			
		int sign = (int) Math.signum(type.bonusForUnitAfterMovingAttack + type.bonusForUnitAfterMovingDefence);
		this.result.setProperty("sign", sign);
		if (type.bonusForUnitAfterMovingAttack != 0 || type.bonusForUnitAfterMovingDefence != 0)
			handleAfterMoveEffect();
			
		return true;
	}
	
	private void handleAfterMoveEffect()
	{
		final ArrayList<Unit> units = new ArrayList<Unit>();
		ActionHandlerHelper.forUnitsInRange(this.targetI, this.targetJ, this.unit.type.bonusAfterMovingRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				if (ActionHandlerUnitMove.this.unit.player == targetUnit.player)
					units.add(targetUnit);
				return false;
			}
		});
		
		for (Unit targetUnit : units)
		{
			if (this.unit.type.bonusForUnitAfterMovingAttack != 0)
			{
				targetUnit.attack += this.unit.type.bonusForUnitAfterMovingAttack;
				Task task = new Task(TaskType.TASK_INCREASE_UNIT_ATTACK, GameHandler.amountPlayers);
				task.setProperty("unit", targetUnit);
				task.setProperty("value", -this.unit.type.bonusForUnitAfterMovingAttack);
				TaskHandler.addNewTask(task);
			}
			if (this.unit.type.bonusForUnitAfterMovingDefence != 0)
			{
				targetUnit.defence += this.unit.type.bonusForUnitAfterMovingDefence;
				Task task = new Task(TaskType.TASK_INCREASE_UNIT_DEFENSE, GameHandler.amountPlayers);
				task.setProperty("unit", targetUnit);
				task.setProperty("value", -this.unit.type.bonusForUnitAfterMovingDefence);
				TaskHandler.addNewTask(task);
			}
		}
		
		this.result.setProperty("units", units.toArray(new Unit[0]));
	}
	
}
