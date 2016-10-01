package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public abstract class ScriptFrom extends Script
{

	public int i;
	public int j;

	public ScriptFrom()
	{}

	public ScriptFrom(int i, int j)
	{
		this.i = i;
		this.j = j;
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

	public ScriptFrom fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
