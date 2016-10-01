package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusAttackAlways extends Bonus {

	public int bonusAttack;
	public int bonusDefence;

	public BonusAttackAlways() {}

	public BonusAttackAlways(int bonusAttack, int bonusDefence) {
		this.bonusAttack = bonusAttack;
		this.bonusDefence = bonusDefence;
	}

	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit) {
		return bonusAttack;
	}

	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit) {
		return bonusDefence;
	}

	@Override
	public int getSign() {
		return (int) Math.signum(bonusAttack + bonusDefence);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
		return object;
	}

	public BonusAttackAlways fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
		return this;
	}
}
