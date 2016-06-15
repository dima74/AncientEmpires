package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJsonHelper;

public abstract class ConditionBoolean extends Condition
{

	public Script[] scripts;
	
	public ConditionBoolean()
	{}
	
	public ConditionBoolean(Script... scripts)
	{
		this.scripts = scripts;
	}
	
	public void resolveAliases(Script[] scripts)
	{
		super.resolveAliases(scripts);
		resolveAliases(this.scripts, scripts);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.add("scripts", SerializableJsonHelper.toJsonArrayNumbered(scripts));
		return object;
	}

	public ConditionBoolean fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		scripts = Script.newInstanceArrayNumbered(object.get("scripts").getAsJsonArray(), info);
		return this;
	}

}
