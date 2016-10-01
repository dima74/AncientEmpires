package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;

public class ConditionAnd extends ConditionBoolean {

	public ConditionAnd() {}

	public ConditionAnd(Script... scripts) {
		super(scripts);
	}

	@Override
	public boolean check() {
		for (Script script : scripts)
			if (!script.checkGeneral())
				return false;
		return true;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		return object;
	}

	public ConditionAnd fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}
}
