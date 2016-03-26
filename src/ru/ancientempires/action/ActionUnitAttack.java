package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.action.result.AttackResult;
import ru.ancientempires.bonuses.BonusCreate;
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
		return targetCell.getTeam() != unit.player.team && unit.canDestroy(targetCell.type);
	}
	
	private boolean checkAttackUnit()
	{
		Unit targetUnit = game.fieldUnits[targetI][targetJ];
		return targetUnit != null && unit.player.team != targetUnit.player.team;
	}
	
	@Override
	public void performQuick()
	{
		unit = game.fieldUnits[i][j];
		if (checkAttackUnit())
			attackUnit();
		else
			attackCell();
	}
	
	private void attackCell()
	{
		result.isAttackUnit = false;
		
		Cell targetCell = game.fieldCells[targetI][targetJ];
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
		targetCell.destroy();
		unit.setTurn();
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
		for (Unit unit : result.unitsToUpdate)
			unit.setTurn();
		unit.setTurn();
	}
	
	private AttackResult attack(Unit unit, Unit targetUnit, boolean reverse)
	{
		int decreaseHealth = new UnitHelper(game).getDecreaseHealth(unit, targetUnit);
		targetUnit.health -= decreaseHealth;
		new UnitHelper(game).checkDied(targetUnit);
		unit.experience += new UnitHelper(game).getQualitySum(targetUnit) * decreaseHealth;
		
		AttackResult result = new AttackResult();
		result.i = unit.i;
		result.j = unit.j;
		result.targetI = targetUnit.i;
		result.targetJ = targetUnit.j;
		result.decreaseHealth = decreaseHealth;
		result.isTargetLive = targetUnit.health > 0;
		unit.updateName();
		if (unit.isLevelUp())
		{
			result.isLevelUp = true;
			result.isPromotion = unit.levelUp();
		}
		result.effectSign = reverse ? 0 : handleAfterAttackEffect(unit, targetUnit);
		return result;
	}
	
	private int handleAfterAttackEffect(Unit unit, Unit targetUnit)
	{
		if (unit.type.creators.length == 0)
			return 0;
		// TODO если у типа есть несколько сreators
		BonusCreate[] creates = unit.type.creators[0].applyBonusesAfterAttack(game, unit, targetUnit);
		int sign = 0;
		for (BonusCreate create : creates)
			sign += create.bonus.getSign();
		return sign;
	}
	
}
