package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.Action;
import ru.ancientempires.campaign.scripts.Script;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignRewriteScriptsStatus extends Action {

	public boolean[] isStarting;
	public boolean[] isFinishing;

	public ActionCampaignRewriteScriptsStatus() {}

	public ActionCampaignRewriteScriptsStatus(Script[] scripts) {
		isStarting = new boolean[scripts.length];
		isFinishing = new boolean[scripts.length];
		for (int i = 0; i < scripts.length; i++) {
			isStarting[i] = scripts[i].isStarting;
			isFinishing[i] = scripts[i].isFinishing;
		}
	}

	@Override
	public void performQuick() {
		// Не "if (game.isMain)", ибо только после загрузки происходит присваивание game.isMain = true
		if (game.campaign.scripts != null) // ибо в GameSaver нам можно не применять, ведь это всё равно не будет там использоваться
			for (int i = 0; i < game.campaign.scripts.length; i++) {
				game.campaign.scripts[i].isStarting = isStarting[i];
				game.campaign.scripts[i].isFinishing = isFinishing[i];
			}
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
		ru.ancientempires.serializable.SerializableDataHelper.toDataArray(output, isStarting);
		ru.ancientempires.serializable.SerializableDataHelper.toDataArray(output, isFinishing);
	}

	public ActionCampaignRewriteScriptsStatus fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		isStarting = ru.ancientempires.serializable.SerializableDataHelper.fromDataArrayBoolean(input);
		isFinishing = ru.ancientempires.serializable.SerializableDataHelper.fromDataArrayBoolean(input);
		return this;
	}

}
