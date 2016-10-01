package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignRemoveUnit;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitDie extends Script
{

	private int i;
	private int j;

	public ScriptUnitDie()
	{}

	public ScriptUnitDie(int i, int j)
	{
		this.i = i;
		this.j = j;
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.unitDie(i, j, this);
	}

	@Override
	public void performAction()
	{
		new ActionCampaignRemoveUnit()
				.setIJ(i, j)
				.perform(game);
	}

	@Override
	public boolean isSimple()
	{
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		return object;
	}

	public ScriptUnitDie fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
