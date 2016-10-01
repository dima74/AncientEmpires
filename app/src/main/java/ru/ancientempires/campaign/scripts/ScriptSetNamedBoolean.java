package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignSetNamedBoolean;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetNamedBoolean extends Script {

	public String  name;
	public boolean bool;

	public ScriptSetNamedBoolean() {}

	public ScriptSetNamedBoolean(String name, boolean bool) {
		this.name = name;
		this.bool = bool;
	}

	@Override
	public void start() {
		new ActionCampaignSetNamedBoolean(name, bool)
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		object.addProperty("bool", bool);
		return object;
	}

	public ScriptSetNamedBoolean fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		bool = object.get("bool").getAsBoolean();
		return this;
	}

}
