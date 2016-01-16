package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import android.graphics.Canvas;

public class GameDrawOnFramesGroup extends GameDrawOnFrames
{
	
	public ArrayList<GameDrawOnFrames> draws = new ArrayList<GameDrawOnFrames>();
	
	public void add(GameDrawOnFrames gameDraw)
	{
		draws.add(gameDraw);
		int frameStart = Math.max(iFrame() + 1, getFrameStart());
		int frameEnd = getFrameEnd();
		animate(frameEnd - frameStart + 1);
		super.increaseFrameStart(frameStart - iFrame() - 1);
	}
	
	public int getFrameStart()
	{
		int frameStart = Integer.MAX_VALUE;
		for (GameDrawOnFrames gameDrawOnFrames : draws)
			frameStart = Math.min(frameStart, gameDrawOnFrames.frameStart);
		return frameStart;
	}
	
	public int getFrameEnd()
	{
		int frameEnd = 0;
		for (GameDrawOnFrames gameDrawOnFrames : draws)
			frameEnd = Math.max(frameEnd, gameDrawOnFrames.frameEnd);
		return frameEnd;
	}
	
	@Override
	public GameDrawOnFrames increaseFrameStart(int framesBeforeStart)
	{
		super.increaseFrameStart(framesBeforeStart);
		for (GameDrawOnFrames draw : draws)
			draw.increaseFrameStart(framesBeforeStart);
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		for (GameDrawOnFrames gameDrawOnFrames : draws)
			gameDrawOnFrames.draw(canvas);
	}
	
}
