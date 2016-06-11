package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignRemoveAllUnits extends Action
{

	@Override
	public void performQuick()
	{
		for (Player player : game.players)
		{
			for (Unit unit : player.units)
				game.removeUnit(unit.i, unit.j);
			player.units.clear();
		}
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
	}

	public ActionCampaignRemoveAllUnits fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		return this;
	}

}
