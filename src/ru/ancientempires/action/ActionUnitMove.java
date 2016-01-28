package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.bonuses.BonusCreate;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class ActionUnitMove extends ActionFromTo
{
	
	private ActionResultUnitMove	result	= new ActionResultUnitMove();
	private Unit					unit;
	
	@Override
	public ActionResultUnitMove perform(Game game)
	{
		performBase(game);
		return result;
	}
	
	@Override
	public boolean check()
	{
		if (!super.check())
			return false;
		Unit unit = game.fieldUnits[i][j];
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
			
		handleAfterMoveEffect();
	}
	
	private void handleAfterMoveEffect()
	{
		if (unit.type.creators.length == 0)
			return;
		// TODO если у типа есть несколько сreators
		BonusCreate[] creates = unit.type.creators[0].applyBonusesAfterMove(game, unit);
		result.sign = 0;
		for (BonusCreate create : creates)
			result.sign += create.bonus.getSign();
		result.units = new Unit[creates.length];
		for (int i = 0; i < creates.length; i++)
			result.units[i] = creates[i].unit;
	}
	
	@Override
	public String toString()
	{
		return String.format("Move (%d %d)->(%d %d) (%s %s)", i, j, targetI, targetJ, game.fieldUnits[i][j], game.fieldUnits[targetI][targetJ]);
	}
	
}
