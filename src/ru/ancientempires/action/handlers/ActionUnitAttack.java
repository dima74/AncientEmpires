package ru.ancientempires.action.handlers;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionUnitAttack extends ActionFromTo
{
	
	private Unit	unit;
	private Unit	targetUnit;
	private Cell	targetCell;
	
	@Override
	public ActionResult action()
	{
		result.successfully = tryAttack();
		return result;
	}
	
	private boolean tryAttack()
	{
		return checkAttack() && (checkAttackUnit() && attackUnit() || checkAttackCell() && attackCell());
	}
	
	private boolean checkAttack()
	{
		if (!GameHandler.checkCoord(i, j) || !GameHandler.checkCoord(targetI, targetJ))
			return false;
			
		unit = GameHandler.fieldUnits[i][j];
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
		targetCell = GameHandler.fieldCells[targetI][targetJ];
		return targetCell.type.isDestroying && canAttackCell();
	}
	
	private boolean canAttackCell()
	{
		return targetCell.getTeam() != unit.player.team;
	}
	
	private boolean attackCell()
	{
		if (targetCell.player != null)
			GameHandler.game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
			
		result.setProperty("isAttackUnit", false);
		targetCell.isDestroying = true;
		targetCell.isCapture = false;
		targetCell.player = null;
		
		unit.isTurn = true;
		
		return true;
	}
	
	private boolean checkAttackUnit()
	{
		targetUnit = GameHandler.fieldUnits[targetI][targetJ];
		return targetUnit != null && canAttackUnit();
	}
	
	private boolean canAttackUnit()
	{
		return unit.player != targetUnit.player;
	}
	
	private boolean attackUnit()
	{
		result.setProperty("isAttackUnit", true);
		
		AttackResult attackResultDirect = attack(unit, targetUnit, false);
		AttackResult attackResultReverse = boundsIsNorm(targetUnit, i, j, true) && targetUnit.health > 0 ? attack(targetUnit, unit, true) : null;
		unit.isTurn = true;
		
		result.setProperty("attackResultDirect", attackResultDirect);
		boolean isReverseAttack = attackResultReverse != null;
		result.setProperty("isReverseAttack", isReverseAttack);
		if (isReverseAttack)
			result.setProperty("attackResultReverse", attackResultReverse);
			
		result.setProperty("unitsToUpdate", ActionHandlerHelper.getUnitsChangedStateNearCell(unit.player, targetI, targetJ));
		
		return true;
	}
	
	private AttackResult attack(Unit unit, Unit targetUnit, boolean reverse)
	{
		int decreaseHealth = UnitHelper.getDecreaseHealth(unit, targetUnit);
		targetUnit.health -= decreaseHealth;
		UnitHelper.checkDied(targetUnit);
		unit.experience += UnitHelper.getQualitySum(targetUnit) * decreaseHealth;
		boolean isLevelUp = UnitHelper.checkLevelUp(unit);
		return new AttackResult(unit.i, unit.j, targetUnit.i, targetUnit.j, decreaseHealth,
				targetUnit.health > 0, isLevelUp, reverse ? 0 : UnitHelper.handleAfterAttackEffect(unit, targetUnit));
	}
	
}
