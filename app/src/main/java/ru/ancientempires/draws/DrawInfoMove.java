package ru.ancientempires.draws;

import android.graphics.Canvas;

import ru.ancientempires.draws.onframes.DrawOnFrames;

public class DrawInfoMove extends DrawOnFrames {

	public DrawInfoMove(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public void startShow() {
		int frameCount = 0;
		for (int y = main.infoY; y != 0; y /= 2)
			frameCount++;
		animate(frameCount);

	}

	@Override
	public void drawOnFrames(Canvas canvas) {
		main.infoY /= 2;
	}

}
