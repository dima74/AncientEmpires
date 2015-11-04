package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public abstract class GameDrawOnFrames extends GameDraw
{
	
	public int		frameCount;
	public int		framePass;
	public int		frameLeft	= 0;
	public int		frameStart;
	public int		frameEnd;
	public boolean	isEndDrawing;
	
	public GameDrawOnFrames(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawOnFrames animate(int frameCount)
	{
		this.frameStart = this.gameDraw.iFrame + 1;
		this.frameEnd = this.frameStart + frameCount - 1;
		this.framePass = 0;
		this.frameLeft = this.frameCount = frameCount;
		this.isEndDrawing = false;
		return this;
	}
	
	public GameDrawOnFrames increaseFrameStart(int framesBeforeStart)
	{
		this.frameStart += framesBeforeStart;
		this.frameEnd += framesBeforeStart;
		return this;
	}
	
	@Override
	public final void draw(Canvas canvas)
	{
		if (this.frameLeft == 0 || this.gameDraw.iFrame < this.frameStart)
			return;
		this.frameLeft--;
		drawOnFrames(canvas);
		if (this.frameLeft == 0)
		{
			onEndDraw();
			this.isEndDrawing = true;
		}
		this.framePass++;
	}
	
	public abstract void drawOnFrames(Canvas canvas);
	
	public void onEndDraw()
	{}
	
	public void reAnimate()
	{
		animate(this.frameCount);
	}
	
}
