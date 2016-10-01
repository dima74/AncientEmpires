package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetMapPosition extends Script
{

	private int i;
	private int j;

	public ScriptSetMapPosition()
	{}

	public ScriptSetMapPosition(int i, int j)
	{
		this.i = i;
		this.j = j;
	}

	@Override
	public void start()
	{
		campaign.iDrawCampaign.setMapPosition(i, j);
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

	public ScriptSetMapPosition fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
