package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignActivateStruct;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitActivateStruct extends ScriptOnePoint {

	public ScriptUnitActivateStruct() {}

	public ScriptUnitActivateStruct(Object... point) {
		super(point);
	}

	@Override
	public void start() {
		new ActionCampaignActivateStruct()
				.setIJ(i(), j())
				.perform(game);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptUnitActivateStruct fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}

}
