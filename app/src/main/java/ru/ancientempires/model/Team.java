package ru.ancientempires.model;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Numbered;

public class Team implements Numbered {

	public static Team newInstance(int i, LoaderInfo info) {
		return info.game.teams[i];
	}

	public int      ordinal;
	public Player[] players;

	public Team(int ordinal) {
		this.ordinal = ordinal;
	}

	@Override
	public int getNumber() {
		return ordinal;
	}

	@Override
	public boolean equals(Object o) {
		Team team = (Team) o;
		if (players.length != team.players.length)
			return false;
		for (int i = 0; i < players.length; i++)
			if (players[i].ordinal != team.players[i].ordinal)
				return false;
		return true;
	}
}
