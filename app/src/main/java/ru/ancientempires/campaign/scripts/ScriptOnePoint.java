package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.serializable.LoaderInfo;

public abstract class ScriptOnePoint extends AbstractScriptOnePoint {

	public AbstractPoint point;

	public ScriptOnePoint() {}

	public ScriptOnePoint(Object... points) {
		this.point = AbstractPoint.createPoint(points);
	}

	public int i() {
		return point.getI();
	}

	public int j() {
		return point.getJ();
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.add("point", point.toJson());
		return object;
	}

	public ScriptOnePoint fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		point = info.fromJson((JsonObject) object.get("point"), AbstractPoint.class);
		return this;
	}
}
