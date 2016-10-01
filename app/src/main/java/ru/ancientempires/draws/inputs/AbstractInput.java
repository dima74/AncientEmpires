package ru.ancientempires.draws.inputs;

import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.model.Game;

public class AbstractInput {

	public Game      game;
	public InputMain inputMain;
	public DrawMain  drawMain;

	public AbstractInput(InputMain inputMain) {
		this.inputMain = inputMain;
		if (inputMain != null) {
			game = inputMain.game;
			drawMain = inputMain.drawMain;
		}
	}

}
