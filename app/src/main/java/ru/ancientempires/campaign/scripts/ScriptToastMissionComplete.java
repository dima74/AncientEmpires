package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptToastMissionComplete extends Script {

	@Localize private String text;

	public ScriptToastMissionComplete() {}

	public ScriptToastMissionComplete(String text) {
		this.text = text;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.toastTitle(text, this);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("text", text);
		return object;
	}

	public ScriptToastMissionComplete fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		text = ru.ancientempires.Localization.get(object.get("text").getAsString());
		return this;
	}
}
