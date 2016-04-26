package ru.ancientempires.model;

public class Team
{
	
	public Player[] players;
	
	public Team(Player[] players)
	{
		this.players = players;
	}
	
	@Override
	public boolean equals(Object o)
	{
		Team team = (Team) o;
		if (players.length != team.players.length)
			return false;
		for (int i = 0; i < players.length; i++)
			if (players[i].ordinal != team.players[i].ordinal)
				return false;
		return true;
	}
	
}
