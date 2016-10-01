package ru.ancientempires;

public class SimpleTeam {

	public SimplePlayer[] players;

	// для GSON
	public SimpleTeam() {}

	public SimpleTeam(SimplePlayer[] players) {
		this.players = players;
	}
}
