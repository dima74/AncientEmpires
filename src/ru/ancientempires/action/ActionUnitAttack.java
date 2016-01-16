package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.action.result.AttackResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionUnitAttack extends ActionFromTo
{
	
	private ActionResultUnitAttack	result	= new ActionResultUnitAttack();
	private Unit					unit;
	private Unit					targetUnit;
	private Cell					targetCell;
	
	@Override
	public ActionResultUnitAttack perform()
	{
		if (!check(checkAttack() && (checkAttackUnit() || checkAttackCell())))
			return null;
		performQuick();
		return commit(result);
	}
	
	@Override
	public void performQuick()
	{
		if (checkAttackUnit())
			attackUnit();
		else
			attackCell();
	}
	
	private boolean checkAttack()
	{
		if (!game.checkCoordinates(i, j) || !game.checkCoordinates(targetI, targetJ))
			return false;
			
		unit = game.fieldUnits[i][j];
		return unit != null && !unit.isTurn && boundsIsNorm(unit, targetI, targetJ, false);
	}
	
	private boolean boundsIsNorm(Unit unit, int targetI, int targetJ, boolean reverse)
	{
		RangeType type = reverse ? unit.type.attackRangeReverse : unit.type.attackRange;
		boolean[][] field = type.field;
		int size = field.length;
		
		int relI = targetI - unit.i + type.radius;
		int relJ = targetJ - unit.j + type.radius;
		
		return relI >= 0 && relI < size && relJ >= 0 && relJ < size && field[relI][relJ];
	}
	
	private boolean checkAttackCell()
	{
		targetCell = game.fieldCells[targetI][targetJ];
		return targetCell.type.isDestroying && targetCell.getTeam() != unit.player.team;
	}
	
	private void attackCell()
	{
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
			
		result.isAttackUnit = false;
		targetCell.isDestroying = true;
		targetCell.isCapture = false;
		targetCell.player = null;
		
		unit.isTurn = true;
	}
	
	private boolean checkAttackUnit()
	{
		targetUnit = game.fieldUnits[targetI][targetJ];
		return targetUnit != null && unit.player != targetUnit.player;
	}
	
	private void attackUnit()
	{
		result.isAttackUnit = true;
		
		AttackResult attackResultDirect = attack(unit, targetUnit, false);
		AttackResult attackResultReverse = boundsIsNorm(targetUnit, i, j, true) && targetUnit.health > 0 ? attack(targetUnit, unit, true) : null;
		unit.isTurn = true;
		
		result.attackResultDirect = attackResultDirect;
		boolean isReverseAttack = attackResultReverse != null;
		result.isReverseAttack = isReverseAttack;
		if (isReverseAttack)
			result.attackResultReverse = attackResultReverse;
			
		result.unitsToUpdate = new ActionHelper().getUnitsChangedStateNearCell(unit.player, targetI, targetJ);
	}
	
	private AttackResult attack(Unit unit, Unit targetUnit, boolean reverse)
	{
		int decreaseHealth = new UnitHelper().getDecreaseHealth(unit, targetUnit);
		targetUnit.health -= decreaseHealth;
		new UnitHelper().checkDied(targetUnit);
		unit.experience += new UnitHelper().getQualitySum(targetUnit) * decreaseHealth;
		boolean isLevelUp = new UnitHelper().checkLevelUp(unit);
		return new AttackResult(unit.i, unit.j, targetUnit.i, targetUnit.j, decreaseHealth,
				targetUnit.health > 0, isLevelUp, reverse ? 0 : new UnitHelper().handleAfterAttackEffect(unit, targetUnit));
	}
	
}
