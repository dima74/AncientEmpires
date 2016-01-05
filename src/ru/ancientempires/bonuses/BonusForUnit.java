package ru.ancientempires.bonuses;

import ru.ancientempires.model.UnitType;

public class BonusForUnit extends Bonus
{
	
	public UnitType	type;
	
	public BonusForUnit(UnitType type, int value)
	{
		this.type = type;
		this.value = value;
	}
	
}
