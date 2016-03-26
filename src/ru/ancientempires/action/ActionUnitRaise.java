package ru.ancientempires.action;

import ru.ancientempires.action.result.ActionResult;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class ActionUnitRaise extends ActionFromTo
{
	
	@Override
	public ActionResult perform(Game game)
	{
		performBase(game);
		return null;
	}
	
	@Override
	public boolean check()
	{
		if (!super.check())
			return false;
			
		Unit unit = game.fieldUnits[i][j];
		Unit targetUnit = game.fieldUnitsDead[targetI][targetJ];
		return unit != null && targetUnit != null && !unit.isTurn && unit.type.raiseRange.checkAccess(unit, targetUnit);
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.fieldUnits[i][j];
		unit.setTurn();
		
		Unit raisedUnit = new Unit(game, unit.type.raiseUnit, unit.player);
		raisedUnit.i = targetI;
		raisedUnit.j = targetJ;
		raisedUnit.player.units.add(raisedUnit);
		raisedUnit.setTurn();
		game.setUnit(targetI, targetJ, raisedUnit);
		game.fieldUnitsDead[targetI][targetJ] = null;
	}
	
}
