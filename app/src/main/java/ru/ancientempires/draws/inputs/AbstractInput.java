package ru.ancientempires.draws.inputs;

import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.model.Game;

public class AbstractInput
{
	
	public Game      game      = GameActivity.activity.game;
	public InputMain inputMain = InputMain.main;
	public DrawMain  drawMain  = DrawMain.main;
	
}
