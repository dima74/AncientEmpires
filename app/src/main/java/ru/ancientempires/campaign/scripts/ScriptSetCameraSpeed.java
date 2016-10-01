package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetCameraSpeed extends Script {

	private int delta;

	public ScriptSetCameraSpeed() {}

	public ScriptSetCameraSpeed(int delta) {
		this.delta = delta;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.setCameraSpeed(delta, this);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("delta", delta);
		return object;
	}

	public ScriptSetCameraSpeed fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		delta = object.get("delta").getAsInt();
		return this;
	}
}
