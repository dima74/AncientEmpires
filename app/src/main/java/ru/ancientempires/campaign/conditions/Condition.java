package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;

public abstract class Condition extends Script {

	@Override
	public void performAction() {}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		return object;
	}

	public Condition fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}
}
