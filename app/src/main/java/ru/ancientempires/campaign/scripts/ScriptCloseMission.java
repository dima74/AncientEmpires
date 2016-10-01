package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptCloseMission extends Script {

	@Override
	public void start() {
		try {
			campaign.iDrawCampaign.closeMission();
		} catch (Exception e) {
			MyAssert.a(false);
			e.printStackTrace();
		}
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		return object;
	}

	public ScriptCloseMission fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}

}
