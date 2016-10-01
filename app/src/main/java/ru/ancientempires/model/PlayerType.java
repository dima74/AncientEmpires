package ru.ancientempires.model;

import ru.ancientempires.Localization;

public enum PlayerType
{
	PLAYER,
	COMPUTER,
	NONE;

	@Override
	public String toString()
	{
		return Localization.get(name());
	}
}
