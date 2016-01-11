package ru.ancientempires.action.handlers;

import java.util.ArrayList;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.tasks.TaskIncreaseUnitAttack;
import ru.ancientempires.tasks.TaskIncreaseUnitDefence;

public class ActionUnitMove extends ActionFromTo
{
	
	private Unit unit;
	
	@Override
	public ActionResult action()
	{
		result.successfully = tryMoveUnit();
		return result;
	}
	
	private boolean tryMoveUnit()
	{
		return checkMoveUnit() && moveUnit();
	}
	
	private boolean checkMoveUnit()
	{
		if (!(GameHandler.checkCoord(i, j) && GameHandler.checkCoord(targetI, targetJ)))
			return false;
		unit = GameHandler.fieldUnits[i][j];
		Unit targetUnit = GameHandler.fieldUnits[targetI][targetJ];
		return unit != null && (targetUnit == null || targetUnit == unit) && canMoveUnit();
	}
	
	private boolean canMoveUnit()
	{
		return !unit.isMove;
	}
	
	private boolean moveUnit()
	{
		UnitType type = unit.type;
		
		GameHandler.fieldDeadUnits[targetI][targetJ] = null;
		GameHandler.removeUnit(i, j);
		GameHandler.setUnit(targetI, targetJ, unit);
		
		unit.isMove = true;
		unit.isTurn = !ActionHandlerHelper.canUnitAction(unit);
		if (!type.canDoTwoActionAfterOne && !(i == targetI && j == targetJ))
			unit.setTurn();
			
		int sign = (int) Math.signum(type.bonusForUnitAfterMovingAttack + type.bonusForUnitAfterMovingDefence);
		result.setProperty("sign", sign);
		if (type.bonusForUnitAfterMovingAttack != 0 || type.bonusForUnitAfterMovingDefence != 0)
			handleAfterMoveEffect();
			
		return true;
	}
	
	private void handleAfterMoveEffect()
	{
		final ArrayList<Unit> units = new ArrayList<Unit>();
		ActionHandlerHelper.forUnitsInRange(targetI, targetJ, unit.type.bonusAfterMovingRange, new CheckerUnit()
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
						.setTurn(GameHandler.amountPlayers)
						.register();
			}
			if (unit.type.bonusForUnitAfterMovingDefence != 0)
			{
				targetUnit.defence += unit.type.bonusForUnitAfterMovingDefence;
				new TaskIncreaseUnitDefence()
						.setUnit(targetUnit)
						.setValue(-unit.type.bonusForUnitAfterMovingDefence)
						.setTurn(GameHandler.amountPlayers)
						.register();
			}
		}
		
		result.setProperty("units", units.toArray(new Unit[0]));
	}
	
}
