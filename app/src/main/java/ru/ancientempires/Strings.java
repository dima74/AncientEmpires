package ru.ancientempires;

public enum Strings
{
	TEAM,
	GOLD,
	UNITS_LIMIT,
	FIGHT,
	PROMOTION,

	EDITOR_GAME_NAME,
	EDITOR_GAME_HEIGHT,
	EDITOR_GAME_WIDTH,
	EDITOR_GAME_NAME_TEMPLATE;

	@Override
	public String toString()
	{
		return Localization.get(name());
	}

}
