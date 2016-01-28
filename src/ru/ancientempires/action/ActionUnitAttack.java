package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.action.result.AttackResult;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.handler.UnitHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class ActionUnitAttack extends ActionFromTo
{
	
	private ActionResultUnitAttack	result	= new ActionResultUnitAttack();
	private Unit					unit;
	
	@Override
	public ActionResultUnitAttack perform(Game game)
	{
		performBase(game);
		return result;
	}
	
	@Override
	public boolean check()
	{
		return super.check() && checkAttack() && (checkAttackUnit() || checkAttackCell());
	}
	
	private boolean checkAttack()
	{
		unit = game.fieldUnits[i][j];
		return unit != null && !unit.isTurn && unit.type.attackRange.checkAccess(unit, targetI, targetJ);
	}
	
	private boolean checkAttackCell()
	{
		Cell targetCell = game.fieldCells[targetI][targetJ];
		return targetCell.type.isDestroying && targetCell.getTeam() != unit.player.team;
	}
	
	private boolean checkAttackUnit()
	{
		Unit targetUnit = game.fieldUnits[targetI][targetJ];
		return targetUnit != null && unit.player != targetUnit.player;
	}
	
	@Override
	public void performQuick()
	{
		unit = game.fieldUnits[i][j];
		if (checkAttackCell())
			attackCell();
		else
			attackUnit();
	}
	
	private void attackCell()
	{
		result.isAttackUnit = false;
		
		Cell targetCell = game.fieldCells[targetI][targetJ];
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
			
		targetCell.isDestroy = true;
		targetCell.isCapture = false;
		targetCell.player = null;
		
		unit.isTurn = true;
	}
	
	private void attackUnit()
	{
		Unit targetUnit = game.fieldUnits[targetI][targetJ];
		result.isAttackUnit = true;
		
		AttackResult attackResultDirect = attack(unit, targetUnit, false);
		AttackResult attackResultReverse = targetUnit.type.attackRangeReverse.checkAccess(targetUnit, unit) && targetUnit.health > 0 ? attack(targetUnit, unit, true) : null;
		
		result.attackResultDirect = attackResultDirect;
		boolean isReverseAttack = attackResultReverse != null;
		result.isReverseAttack = isReverseAttack;
		if (isReverseAttack)
			result.attackResultReverse = attackResultReverse;
			
		result.unitsToUpdate = new ActionHelper(game).getUnitsChangedStateNearCell(unit.player, targetI, targetJ);
		unit.isTurn = true;
	}
	
	private AttackResult attack(Unit unit, Unit targetUnit, boolean reverse)
	{
		int decreaseHealth = new UnitHelper(game).getDecreaseHealth(unit, targetUnit);
		targetUnit.health -= decreaseHealth;
		new UnitHelper(game).checkDied(targetUnit);
		unit.experience += new UnitHelper(game).getQualitySum(targetUnit) * decreaseHealth;
		boolean isLevelUp = new UnitHelper(game).checkLevelUp(unit);
		return new AttackResult(unit.i, unit.j, targetUnit.i, targetUnit.j, decreaseHealth,
				targetUnit.health > 0, isLevelUp, reverse ? 0 : new UnitHelper(game).handleAfterAttackEffect(unit, targetUnit));
	}
	
}
