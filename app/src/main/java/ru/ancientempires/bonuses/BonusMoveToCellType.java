package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusMoveToCellType extends Bonus {

	public CellType group;
	public int      bonus;

	public BonusMoveToCellType() {}

	public BonusMoveToCellType(CellType group, int bonus) {
		this.group = group;
		this.bonus = bonus;
	}

	@Override
	public int getBonusMove(Game game, Unit unit, Cell targetCell) {
		return group == targetCell.type ? bonus : 0;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("group", group.getNumber());
		object.addProperty("bonus", bonus);
		return object;
	}

	public BonusMoveToCellType fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		group = CellType.newInstance(object.get("group").getAsInt(), info);
		bonus = object.get("bonus").getAsInt();
		return this;
	}
}
