package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.serializable.LoaderInfo;

public class PointInteger extends AbstractPoint {

	public int i, j;

	public PointInteger() {
	}

	public PointInteger(int i, int j) {
		this.i = i;
		this.j = j;
	}

	@Override
	public int getI() {
		return i;
	}

	@Override
	public int getJ() {
		return j;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		return object;
	}

	public PointInteger fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
		output.writeInt(i);
		output.writeInt(j);
	}

	public PointInteger fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		i = input.readInt();
		j = input.readInt();
		return this;
	}
}
