package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.Action;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignLeaveCampaign extends Action {

	@Override
	public void performQuick() {
		if (game.isSaver)
			game.path.leaveCampaign();
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
	}

	public ActionCampaignLeaveCampaign fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		return this;
	}
}
