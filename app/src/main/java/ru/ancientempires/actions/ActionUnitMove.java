package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.result.ActionResultUnitMove;
import ru.ancientempires.bonuses.BonusCreate;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionUnitMove extends ActionFromTo
{
	
	private ActionResultUnitMove result = new ActionResultUnitMove();
	@Exclude private Unit unit;
	
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
		// TODO ещё можно было бы проверять, что существует корректный путь
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
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionUnitMove fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
