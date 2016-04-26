package ru.ancientempires;

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
