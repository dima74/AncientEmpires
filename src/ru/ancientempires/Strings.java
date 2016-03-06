package ru.ancientempires;

public enum Strings
{
	TEAM,
	GOLD,
	UNITS_LIMIT,
	FIGHT,
	PROMOTION;
	
	@Override
	public String toString()
	{
		return Localization.get(name());
	};
}
