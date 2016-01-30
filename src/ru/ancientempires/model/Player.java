package ru.ancientempires.model;

import java.util.ArrayList;

import ru.ancientempires.MyColor;
import ru.ancientempires.PlayerType;

public class Player
{
	
	public MyColor	color;
	public int		ordinal;
	
	public PlayerType		type;
	public Team				team	= new Team(new Player[]
										{
												this
										});
	public ArrayList<Unit>	units	= new ArrayList<Unit>();
	public int				gold;
	
	public int	cursorI;
	public int	cursorJ;
	
	@Override
	public boolean equals(Object o)
	{
		Player player = (Player) o;
		if (color != player.color)
			return false;
		if (ordinal != player.ordinal)
			return false;
		if (type != player.type)
			return false;
		if (gold != player.gold)
			return false;
		if (cursorI != player.cursorI)
			return false;
		if (cursorJ != player.cursorJ)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return "Player [" + units + "]";
	}
	
}