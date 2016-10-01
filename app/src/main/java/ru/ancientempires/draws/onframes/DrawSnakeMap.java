package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawSnakeMap extends DrawOnFrames {

	public DrawSnakeMap(BaseDrawMain mainBase) {
		super(mainBase);
	}

	@Override
	public void drawOnFrames(Canvas canvas) {
		if (iFrame() % 2 == 0) {
			main.extraOffsetY = random.nextInt() % 4;
			main.extraOffsetX = random.nextInt() % 10;
		}
	}

	@Override
	public void onEnd() {
		main.extraOffsetY = 0;
		main.extraOffsetX = 0;
	}

}
