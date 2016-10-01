package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusAttackForUnit extends Bonus {

	public UnitType targetType;
	public int      bonusAttack;
	public int      bonusDefence;

	public BonusAttackForUnit() {}

	public BonusAttackForUnit(UnitType targetType, int bonusAttack, int bonusDefence) {
		this.targetType = targetType;
		this.bonusAttack = bonusAttack;
		this.bonusDefence = bonusDefence;
	}

	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit) {
		return checkUnit(targetUnit) ? bonusAttack : 0;
	}

	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit) {
		return checkUnit(fromUnit) ? bonusDefence : 0;
	}

	public boolean checkUnit(Unit unit) {
		return unit != null && unit.type == targetType;
	}

	@Override
	public int getSign() {
		return (int) Math.signum(bonusAttack + bonusDefence);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("targetType", targetType.getName());
		object.addProperty("bonusAttack", bonusAttack);
		object.addProperty("bonusDefence", bonusDefence);
		return object;
	}

	public BonusAttackForUnit fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		targetType = UnitType.newInstance(object.get("targetType").getAsString(), info);
		bonusAttack = object.get("bonusAttack").getAsInt();
		bonusDefence = object.get("bonusDefence").getAsInt();
		return this;
	}

}
