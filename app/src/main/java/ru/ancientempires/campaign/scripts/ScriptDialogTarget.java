package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Localize;

public class ScriptDialogTarget extends Script {

	@Localize private String textTitle;
	@Localize private String textTarget;

	public ScriptDialogTarget() {}

	public ScriptDialogTarget(String textTitle, String textTarget) {
		this.textTitle = textTitle;
		this.textTarget = textTarget;
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.dialogTarget(textTitle, textTarget, this);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("textTitle", textTitle);
		object.addProperty("textTarget", textTarget);
		return object;
	}

	public ScriptDialogTarget fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		textTitle = ru.ancientempires.Localization.get(object.get("textTitle").getAsString());
		textTarget = ru.ancientempires.Localization.get(object.get("textTarget").getAsString());
		return this;
	}
}
