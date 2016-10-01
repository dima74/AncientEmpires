package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.Action;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignSetNamedBoolean extends Action {

	public String  name;
	public boolean bool;

	public ActionCampaignSetNamedBoolean() {}

	public ActionCampaignSetNamedBoolean(String name, boolean bool) {
		this.name = name;
		this.bool = bool;
	}

	@Override
	public void performQuick() {
		game.namedBooleans.set(name, bool);
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
		output.writeUTF(name);
		output.writeBoolean(bool);
	}

	public ActionCampaignSetNamedBoolean fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		name = input.readUTF();
		bool = input.readBoolean();
		return this;
	}

}
