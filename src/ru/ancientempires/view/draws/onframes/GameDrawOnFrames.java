package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawOnFrames extends GameDraw
{
	
	public boolean	isDrawing		= false;
	public boolean	isEndDrawing	= false;
	
	public int		frameLength		= -1;
	public int		frameStart		= -1;
	public int		frameEnd		= -1;
	
	public GameDrawOnFrames(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawOnFrames animate(int frameToStart, int frameLength)
	{
		this.frameLength = frameLength;
		this.frameStart = this.gameDraw.iFrame + frameToStart;
		this.frameEnd = this.frameStart + frameLength;
		return this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		this.isDrawing = this.frameStart <= this.gameDraw.iFrame && this.gameDraw.iFrame < this.frameEnd;
		this.isEndDrawing = this.gameDraw.iFrame >= this.frameEnd;
	}
	
	public void reAnimate()
	{
		this.frameStart = this.gameDraw.iFrame;
		this.frameEnd = this.frameStart + this.frameLength;
	}
	
}
