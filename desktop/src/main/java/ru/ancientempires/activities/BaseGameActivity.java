package ru.ancientempires.activities;

import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.model.Game;
import ru.ancientempires.swing.GameComponent;

public class BaseGameActivity {

	public GameComponent view;
	public Game          game;
	public BaseDrawMain  drawMain;

	public GameComponent getView() {
		return view;
	}

	public float getMapScale() {
		return 2;
	}

	public void setMapScale(float scale) {}
}
