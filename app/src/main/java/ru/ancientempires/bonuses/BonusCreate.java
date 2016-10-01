package ru.ancientempires.bonuses;

import ru.ancientempires.model.Unit;

public class BonusCreate {

	public Unit  unit;
	public Bonus bonus;

	public BonusCreate(Unit unit, Bonus bonus) {
		this.unit = unit;
		this.bonus = bonus;
	}
}
