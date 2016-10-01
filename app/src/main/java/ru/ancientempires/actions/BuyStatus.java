package ru.ancientempires.actions;

import ru.ancientempires.Localization;

public enum BuyStatus
{
	SUCCESS,
	NO_GOLD,
	NO_PLACE,
	UNIT_LIMIT_REACHED;

	@Override
	public String toString()
	{
		return Localization.get(name());
	}

}
