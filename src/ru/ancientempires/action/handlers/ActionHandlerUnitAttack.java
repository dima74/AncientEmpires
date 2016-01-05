package ru.ancientempires.action.handlers;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.action.AttackResult;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;

public class ActionHandlerUnitAttack extends ActionHandler
{
	
	public ActionHandlerUnitAttack(ActionType type)
	{
		super(type);
	}
	
	@Override
	public ActionHandler newInstance()
	{
		return new ActionHandlerUnitAttack(this.type);
	}
	
	private int	i;
	private int	j;
	private int	targetI;
	private int	targetJ;
	
	private Unit	unit;
	private Unit	targetUnit;
	private Cell	targetCell;
	
	@Override
	public ActionResult action(Action action)
	{
		this.i = (int) action.getProperty("i");
		this.j = (int) action.getProperty("j");
		this.targetI = (int) action.getProperty("targetI");
		this.targetJ = (int) action.getProperty("targetJ");
		
		this.result.successfully = tryAttack();
		return this.result;
	}
	
	private boolean tryAttack()
	{
		return checkAttack() && (checkAttackUnit() && attackUnit() || checkAttackCell() && attackCell());
	}
	
	private boolean checkAttack()
	{
		if (!GameHandler.checkCoord(this.i, this.j) || !GameHandler.checkCoord(this.targetI, this.targetJ))
			return false;
			
		this.unit = GameHandler.fieldUnits[this.i][this.j];
		return this.unit != null && !this.unit.isTurn && boundsIsNorm(this.unit, this.targetI, this.targetJ, false);
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
		this.targetCell = GameHandler.fieldCells[this.targetI][this.targetJ];
		return this.targetCell.type.isDestroying && canAttackCell();
	}
	
	private boolean canAttackCell()
	{
		return this.targetCell.getTeam() != this.unit.player.team;
	}
	
	private boolean attackCell()
	{
		if (this.targetCell.player != null)
			GameHandler.game.currentEarns[this.targetCell.player.ordinal] -= this.targetCell.type.earn;
			
		this.result.setProperty("isAttackUnit", false);
		this.targetCell.isDestroying = true;
		this.targetCell.isCapture = false;
		this.targetCell.player = null;
		
		this.unit.isTurn = true;
		
		return true;
	}
	
	private boolean checkAttackUnit()
	{
		this.targetUnit = GameHandler.fieldUnits[this.targetI][this.targetJ];
		return this.targetUnit != null && canAttackUnit();
	}
	
	private boolean canAttackUnit()
	{
		return this.unit.player != this.targetUnit.player;
	}
	
	private boolean attackUnit()
	{
		this.result.setProperty("isAttackUnit", true);
		
		AttackResult attackResultDirect = attack(this.unit, this.targetUnit, false);
		AttackResult attackResultReverse = boundsIsNorm(this.targetUnit, this.i, this.j, true) && this.targetUnit.health > 0 ? attack(this.targetUnit, this.unit, true) : null;
		this.unit.isTurn = true;
		
		this.result.setProperty("attackResultDirect", attackResultDirect);
		boolean isReverseAttack = attackResultReverse != null;
		this.result.setProperty("isReverseAttack", isReverseAttack);
		if (isReverseAttack)
			this.result.setProperty("attackResultReverse", attackResultReverse);
			
		this.result.setProperty("unitsToUpdate", ActionHandlerHelper.getUnitsChangedStateNearCell(this.unit.player, this.targetI, this.targetJ));
		
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
