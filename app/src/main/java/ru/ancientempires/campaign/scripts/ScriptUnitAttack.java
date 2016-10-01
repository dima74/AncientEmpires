package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitAttack extends ScriptFrom {

	public ScriptUnitAttack() {}

	public ScriptUnitAttack(int i, int j) {
		super(i, j);
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.unitAttack(i, j, this);
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

	public ScriptUnitAttack fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}

}
