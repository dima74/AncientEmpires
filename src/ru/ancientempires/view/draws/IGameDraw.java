package ru.ancientempires.view.draws;

import ru.ancientempires.model.Game;

public class IGameDraw
{
	
	public GameDrawMain	main	= GameDrawMain.main; 
	public Game			game	= main == null ? null : main.game;
	
}
