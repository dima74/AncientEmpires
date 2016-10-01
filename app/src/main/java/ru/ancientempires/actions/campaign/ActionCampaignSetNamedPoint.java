package ru.ancientempires.actions.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.actions.Action;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.serializable.LoaderInfo;

public class ActionCampaignSetNamedPoint extends Action {

	public String        name;
	public AbstractPoint point;

	public ActionCampaignSetNamedPoint() {}

	public ActionCampaignSetNamedPoint(String name, AbstractPoint point) {
		this.name = name;
		this.point = point;
	}

	@Override
	public void performQuick() {
		game.namedPoints.set(name, point);
	}

	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
		output.writeUTF(name);
		point.toData(output);
	}

	public ActionCampaignSetNamedPoint fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		name = input.readUTF();
		point = info.fromData(input, AbstractPoint.class);
		return this;
	}

}
