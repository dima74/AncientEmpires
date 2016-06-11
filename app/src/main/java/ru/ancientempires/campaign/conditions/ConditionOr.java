package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionOr extends ConditionBoolean
{
	
	public ConditionOr()
	{}
	
	public ConditionOr(Script... scripts)
	{
		super(scripts);
	}
	
	@Override
	public boolean check()
	{
		for (Script script : scripts)
			if (script.checkGeneral())
				return true;
		return false;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		return object;
	}

	public ConditionOr fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
