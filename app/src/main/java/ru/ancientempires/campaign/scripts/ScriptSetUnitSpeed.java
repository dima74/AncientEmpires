package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptSetUnitSpeed extends Script {

	private int framesForCell;

	public ScriptSetUnitSpeed() {}

	public ScriptSetUnitSpeed(int framesForCell) {
		this.framesForCell = framesForCell;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.setUnitSpeed(framesForCell, this);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("framesForCell", framesForCell);
		return object;
	}

	public ScriptSetUnitSpeed fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		framesForCell = object.get("framesForCell").getAsInt();
		return this;
	}

}
