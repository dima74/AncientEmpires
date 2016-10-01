package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class ConditionTurn extends Condition {

	private int turn;

	public ConditionTurn() {}

	public ConditionTurn(int turn) {
		this.turn = turn;
	}

	@Override
	public boolean check() {
		return game.currentTurn == turn;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("turn", turn);
		return object;
	}

	public ConditionTurn fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		turn = object.get("turn").getAsInt();
		return this;
	}
}
