package ru.ancientempires.action.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.action.ActionFrom;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignSetNamedUnit extends ActionFrom
{
	
	@Override
	public boolean isCampaign()
	{
		return true;
	}
	
	public String name;

	public ActionCampaignSetNamedUnit()
	{}

	public ActionCampaignSetNamedUnit(String name)
	{
		this.name = name;
	}
	
	@Override
	public void performQuick()
	{
		game.namedUnits.set(name, game.getUnit(i, j));
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
		output.writeUTF(name);
	}

	public ActionCampaignSetNamedUnit fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		name = input.readUTF();
		return this;
	}

}
