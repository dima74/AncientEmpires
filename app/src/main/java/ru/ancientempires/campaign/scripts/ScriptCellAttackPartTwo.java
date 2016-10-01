package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.actions.campaign.ActionCampaignCellAttack;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptCellAttackPartTwo extends ScriptFrom {

	public ScriptCellAttackPartTwo() {
	}

	public ScriptCellAttackPartTwo(int i, int j) {
		super(i, j);
	}

	@Override
	public void start() {
		campaign.iDrawCampaign.cellAttackPartTwo(i, j, this);
	}

	@Override
	public void performAction() {
		new ActionCampaignCellAttack()
				.setIJ(i, j)
				.perform(game);
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

	public ScriptCellAttackPartTwo fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		return this;
	}
}
