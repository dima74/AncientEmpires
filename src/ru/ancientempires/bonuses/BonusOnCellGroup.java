package ru.ancientempires.bonuses;

import ru.ancientempires.model.CellTypeGroup;

public class BonusOnCellGroup extends Bonus
{
	
	public CellTypeGroup group;
	
	public BonusOnCellGroup(CellTypeGroup group, int value)
	{
		this.group = group;
		this.value = value;
	}
	
}
