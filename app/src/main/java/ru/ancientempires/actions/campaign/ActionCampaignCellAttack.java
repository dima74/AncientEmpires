package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.ActionFrom;
import ru.ancientempires.model.Cell;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignCellAttack extends ActionFrom
{

	@Override
	public void performQuick()
	{
		Cell targetCell = game.fieldCells[i][j];
		if (targetCell.player != null)
			game.currentEarns[targetCell.player.ordinal] -= targetCell.type.earn;
		targetCell.destroy();
	}

	@Override
	public boolean check()
	{
		return game.fieldCells[i][j].type.destroyingType != null;
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionCampaignCellAttack fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
