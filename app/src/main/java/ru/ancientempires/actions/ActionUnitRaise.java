package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.result.ActionResult;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionUnitRaise extends ActionFromTo {

	@Override
	public ActionResult perform(Game game) {
		performBase(game);
		return null;
	}

	@Override
	public boolean check() {
		if (!super.check())
			return false;

		Unit unit = game.fieldUnits[i][j];
		Unit targetUnit = game.fieldUnitsDead[targetI][targetJ];
		return unit != null && targetUnit != null && !unit.isTurn && unit.type.raiseRange.checkAccess(unit, targetUnit);
	}

	@Override
	public void performQuick() {
		Unit unit = game.fieldUnits[i][j];
		unit.setTurn();

		Unit raisedUnit = new Unit(game, unit.type.raiseType, unit.player);
		raisedUnit.i = targetI;
		raisedUnit.j = targetJ;
		raisedUnit.player.units.add(raisedUnit);
		raisedUnit.setTurn();
		game.setUnit(targetI, targetJ, raisedUnit);
		game.fieldUnitsDead[targetI][targetJ] = null;
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
	}

	public ActionUnitRaise fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		return this;
	}
}
