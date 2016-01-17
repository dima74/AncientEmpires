package ru.ancientempires.action;

import java.util.ArrayList;

import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.TaskIncreaseUnitAttack;
import ru.ancientempires.tasks.TaskIncreaseUnitDefence;

public class ActionUnitMove extends ActionFromTo
{
	
	private ActionResultUnitMove	result	= new ActionResultUnitMove();
	private Unit					unit;
	
	@Override
	public ActionResultUnitMove perform()
	{
		if (!check(checkMoveUnit()))
			return null;
		performQuick();
		return commit(result);
	}
	
	private boolean checkMoveUnit()
	{
		if (!(game.checkCoordinates(i, j) && game.checkCoordinates(targetI, targetJ)))
			return false;
		unit = game.fieldUnits[i][j];
		Unit targetUnit = game.fieldUnits[targetI][targetJ];
		return new ActionHelper(game).isUnitActive(i, j) && (targetUnit == null || targetUnit == unit) && !unit.isMove;
	}
	
	@Override
	public void performQuick()
	{
		unit = game.fieldUnits[i][j];
		UnitType type = unit.type;
		
		game.fieldUnitsDead[targetI][targetJ] = null;
		game.removeUnit(i, j);
		game.setUnit(targetI, targetJ, unit);
		
		unit.isMove = true;
		unit.isTurn = !new ActionHelper(game).canUnitAction(unit);
		if (!type.canDoTwoActionAfterOne && !(i == targetI && j == targetJ))
			unit.setTurn();
			
		int sign = (int) Math.signum(type.bonusForUnitAfterMovingAttack + type.bonusForUnitAfterMovingDefence);
		result.sign = sign;
		if (type.bonusForUnitAfterMovingAttack != 0 || type.bonusForUnitAfterMovingDefence != 0)
			handleAfterMoveEffect();
	}
	
	private void handleAfterMoveEffect()
	{
		final ArrayList<Unit> units = new ArrayList<Unit>();
		new ActionHelper(game).forUnitsInRange(targetI, targetJ, unit.type.bonusAfterMovingRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				if (unit.player == targetUnit.player)
					units.add(targetUnit);
				return false;
			}
		});
		
		for (Unit targetUnit : units)
		{
			if (unit.type.bonusForUnitAfterMovingAttack != 0)
			{
				targetUnit.attack += unit.type.bonusForUnitAfterMovingAttack;
				new TaskIncreaseUnitAttack()
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterMovingAttack)
						.setTurn(game.amountPlayers())
						.register();
			}
			if (unit.type.bonusForUnitAfterMovingDefence != 0)
			{
				targetUnit.defence += unit.type.bonusForUnitAfterMovingDefence;
				new TaskIncreaseUnitDefence()
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterMovingDefence)
						.setTurn(game.amountPlayers())
						.register();
			}
		}
		
		result.units = units.toArray(new Unit[0]);
	}
	
}
