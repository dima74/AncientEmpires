package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetCursorPosition extends Script
{
	
	private int i;
	private int j;

	public ScriptSetCursorPosition()
	{}
	
	public ScriptSetCursorPosition(int i, int j)
	{
		this.i = i;
		this.j = j;
	}
	
	@Override
	public void start()
	{
		campaign.iDrawCampaign.setCursorPosition(i, j, this);
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

	public ScriptSetCursorPosition fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
