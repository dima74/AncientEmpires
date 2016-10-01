package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.ActionFrom;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignUnitCreate extends ActionFrom
{

	public UnitType type;
	public int      player;

	public ActionCampaignUnitCreate setType(UnitType type)
	{
		this.type = type;
		return this;
	}

	public ActionCampaignUnitCreate setPlayer(int player)
	{
		this.player = player;
		return this;
	}

	@Override
	public void performQuick()
	{
		Player player = game.players[this.player];
		Unit unit = new Unit(game, type, player);
		player.units.add(unit);
		game.setUnit(i, j, unit);
	}

	@Override
	public boolean check()
	{
		return game.canSetUnit(i, j);
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeInt(type.getNumber());
		output.writeInt(player);
	}

	public ActionCampaignUnitCreate fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		type = UnitType.newInstance(input.readInt(), info);
		player = input.readInt();
		return this;
	}

}
