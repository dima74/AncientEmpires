package ru.ancientempires;

public enum Strings
{
	TEAM,
	GOLD,
	UNITS_LIMIT,
	FIGHT;
	
	@Override
	public String toString()
	{
		return Localization.get(name());
	};
}
