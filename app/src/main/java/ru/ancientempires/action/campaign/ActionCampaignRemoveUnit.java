package ru.ancientempires.action.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignRemoveUnit extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}

	@Override
	public void performQuick()
	{
		Unit unit = game.getUnit(i, j);
		game.removeUnit(i, j);
		unit.player.units.remove(unit);
	}

	@Override
	public boolean check()
	{
		return game.getUnit(i, j) != null;
	}
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionCampaignRemoveUnit fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
