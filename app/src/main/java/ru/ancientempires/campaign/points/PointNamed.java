package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.serializable.LoaderInfo;

public class PointNamed extends AbstractPoint {

	public String name;

	public PointNamed() {
	}

	public PointNamed(String name) {
		this.name = name;
	}

	@Override
	public int getI() {
		return game.namedPoints.get(name).getI();
	}

	@Override
	public int getJ() {
		return game.namedPoints.get(name).getJ();
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("name", name);
		return object;
	}

	public PointNamed fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		name = object.get("name").getAsString();
		return this;
	}

	public void toData(DataOutputStream output) throws Exception {
		super.toData(output);
		output.writeUTF(name);
	}

	public PointNamed fromData(DataInputStream input, LoaderInfo info) throws Exception {
		super.fromData(input, info);
		name = input.readUTF();
		return this;
	}

}
