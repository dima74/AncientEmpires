package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignRemoveAllUnits;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptRemoveAllUnits extends Script {

	public ScriptRemoveAllUnits() {}

	@Override
	public void start() {
		performAction();
	}

	@Override
	public void performAction() {
		new ActionCampaignRemoveAllUnits()
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptRemoveAllUnits fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}

}
