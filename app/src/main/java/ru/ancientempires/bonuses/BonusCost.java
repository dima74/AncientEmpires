package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusCost extends Bonus {

	public int multi;

	public BonusCost() {}

	public BonusCost(int multi) {
		this.multi = multi;
	}

	@Override
	public int getBonusCost(Game game, Unit unit) {
		return unit.numberBuys * multi;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("multi", multi);
		return object;
	}

	public BonusCost fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		multi = object.get("multi").getAsInt();
		return this;
	}
}
