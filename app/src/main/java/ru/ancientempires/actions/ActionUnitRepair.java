package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.result.ActionResult;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionUnitRepair extends ActionFrom
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
		return super.check() && new ActionHelper(game).canUnitRepair(i, j);
	}

	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		cell.repair();
		unit.isTurn = true;
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionUnitRepair fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
