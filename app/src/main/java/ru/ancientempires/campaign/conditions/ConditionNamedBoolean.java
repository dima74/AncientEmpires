package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ConditionNamedBoolean extends Condition
{

	public String name;

	public ConditionNamedBoolean()
	{}

	public ConditionNamedBoolean(String name)
	{
		this.name = name;
	}

	@Override
	public boolean check()
	{
		return game.namedBooleans.get(name);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		return object;
	}

	public ConditionNamedBoolean fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		return this;
	}

}
