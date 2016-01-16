package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;

public abstract class GameDrawOnFrames extends GameDraw
{
	
	public int		frameCount;
	public int		framePass;
	public int		frameLeft	= 0;
	public int		frameStart;
	public int		frameEnd;
	public boolean	isEndDrawing;
	
	public GameDrawOnFrames animate(int frameCount)
	{
		frameStart = iFrame() + 1;
		frameEnd = frameStart + frameCount - 1;
		framePass = 0;
		frameLeft = this.frameCount = frameCount;
		isEndDrawing = false;
		return this;
	}
	
	public GameDrawOnFrames increaseFrameStart(int framesBeforeStart)
	{
		frameStart += framesBeforeStart;
		frameEnd += framesBeforeStart;
		return this;
	}
	
	@Override
	public final void draw(Canvas canvas)
	{
		if (frameLeft == 0 || iFrame() < frameStart)
			return;
		frameLeft--;
		drawOnFrames(canvas);
		if (frameLeft == 0)
		{
			onEndDraw();
			isEndDrawing = true;
		}
		framePass++;
	}
	
	public abstract void drawOnFrames(Canvas canvas);
	
	public void onEndDraw()
	{}
	
	public void reAnimate()
	{
		animate(frameCount);
	}
	
}
