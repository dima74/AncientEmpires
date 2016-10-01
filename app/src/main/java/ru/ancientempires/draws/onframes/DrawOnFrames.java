package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.draws.Draw;

public abstract class DrawOnFrames extends Draw {

	public int frameCount;
	public int framePass;
	public int frameLeft = 0;
	public int     frameStart;
	public int     frameEnd;
	public boolean isEndDrawing;

	public DrawOnFrames(BaseDrawMain mainBase) {
		super(mainBase);
	}

	public DrawOnFrames animate(int frameCount) {
		frameStart = iFrame() + 1;
		frameEnd = frameStart + frameCount - 1;
		framePass = 0;
		frameLeft = this.frameCount = frameCount;
		isEndDrawing = false;
		return this;
	}

	public DrawOnFrames increaseFrameStart(int framesBeforeStart) {
		frameStart += framesBeforeStart;
		frameEnd += framesBeforeStart;
		return this;
	}

	public DrawOnFrames setFrameStart(int frameStart) {
		this.frameStart = frameStart;
		frameEnd = frameStart + frameCount;
		return this;
	}

	@Override
	public boolean isEnd() {
		return isEndDrawing;
	}

	@Override
	public final void draw(Canvas canvas) {
		if (frameLeft == 0 || iFrame() < frameStart)
			return;
		if (iFrame() == frameStart)
			onStart();
		frameLeft--;
		drawOnFrames(canvas);
		if (frameLeft == 0) {
			onEnd();
			isEndDrawing = true;
		}
		framePass++;
	}

	public void onStart() {}

	public abstract void drawOnFrames(Canvas canvas);

	public void onEnd() {}

	public void reAnimate() {
		animate(frameCount);
	}

}
