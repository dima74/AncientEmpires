package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptLoaderAlias extends Script
{

	public int i;

	public ScriptLoaderAlias()
	{}

	public ScriptLoaderAlias(int i)
	{
		this.i = i;
	}

	@Override
	public boolean check()
	{
		MyAssert.a(false);
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		return object;
	}

	public ScriptLoaderAlias fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		return this;
	}

}
