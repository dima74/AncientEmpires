package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.result.ActionResult;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionUnitCapture extends ActionFrom
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
		return super.check() && new ActionHelper(game).canUnitCapture(i, j);
	}
	
	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		Cell cell = game.fieldCells[i][j];
		if (cell.player != null)
			game.currentEarns[cell.player.ordinal] -= cell.type.earn;

		cell.player = unit.player;
		unit.setTurn();
		game.currentEarns[unit.player.ordinal] += cell.type.earn;
	}
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionUnitCapture fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
