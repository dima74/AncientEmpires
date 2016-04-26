package ru.ancientempires;

public class SimplePlayer
{
	
	public MyColor		color;
	public int			ordinal;
	public PlayerType	type;
	public int			gold;
	
	public SimplePlayer(MyColor color, int ordinal, PlayerType type, int gold)
	{
		this.color = color;
		this.ordinal = ordinal;
		this.type = type;
		this.gold = gold;
	}
	
}