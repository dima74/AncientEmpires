package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusMoveToCellGroup extends Bonus {

	public CellGroup group;
	public int       bonus;

	public BonusMoveToCellGroup() {}

	public BonusMoveToCellGroup(CellGroup group, int bonus) {
		this.group = group;
		this.bonus = bonus;
	}

	@Override
	public int getBonusMove(Game game, Unit unit, Cell targetCell) {
		return group.contains(targetCell.type) ? bonus : 0;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() {
		JsonObject object = super.toJson();
		object.addProperty("group", group.getNumber());
		object.addProperty("bonus", bonus);
		return object;
	}

	public BonusMoveToCellGroup fromJson(JsonObject object, LoaderInfo info) throws Exception {
		super.fromJson(object, info);
		group = CellGroup.newInstance(object.get("group").getAsInt(), info);
		bonus = object.get("bonus").getAsInt();
		return this;
	}
}
