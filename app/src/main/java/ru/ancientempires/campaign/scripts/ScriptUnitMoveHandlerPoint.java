package ru.ancientempires.campaign.scripts;

import com.google.gson.JsonObject;

import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.serializable.LoaderInfo;

public class ScriptUnitMoveHandlerPoint extends ScriptUnitMoveHandler {

	public AbstractPoint point;

	public ScriptUnitMoveHandlerPoint() {}

	public ScriptUnitMoveHandlerPoint(Object... point) {
		this.point = AbstractPoint.createPoint(point);
	}

	@Override
	public void unitMove(UnitBitmap unit) {
		if (unit.exactlyOn(point))
			complete();
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.add("point", point.toJson());
		return object;
	}

	public ScriptUnitMoveHandlerPoint fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		point = info.fromJson((JsonObject) object.get("point"), AbstractPoint.class);
		return this;
	}
}
