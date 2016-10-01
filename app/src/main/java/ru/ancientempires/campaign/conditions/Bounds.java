package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class Bounds extends AbstractBounds {

	public int iMin;
	public int jMin;
	public int iMax;
	public int jMax;

	public Bounds() {}

	public Bounds(int iMin, int jMin, int iMax, int jMax) {
		this.iMin = iMin;
		this.jMin = jMin;
		this.iMax = iMax;
		this.jMax = jMax;
	}

	public boolean in(int i, int j) {
		return iMin <= i && i <= iMax && jMin <= j && j <= jMax;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("iMin", iMin);
		object.addProperty("jMin", jMin);
		object.addProperty("iMax", iMax);
		object.addProperty("jMax", jMax);
		return object;
	}

	public Bounds fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		iMin = object.get("iMin").getAsInt();
		jMin = object.get("jMin").getAsInt();
		iMax = object.get("iMax").getAsInt();
		jMax = object.get("jMax").getAsInt();
		return this;
	}
}
