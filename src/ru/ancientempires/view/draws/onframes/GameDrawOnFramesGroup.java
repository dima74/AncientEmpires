package ru.ancientempires.view.draws.onframes;

import java.util.ArrayList;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawOnFramesGroup extends GameDrawOnFrames
{
	
	public ArrayList<GameDrawOnFrames> draws = new ArrayList<GameDrawOnFrames>();
	
	public GameDrawOnFramesGroup(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void add(GameDrawOnFrames gameDraw)
	{
		this.draws.add(gameDraw);
		int frameStart = Math.max(this.gameDraw.iFrame + 1, getFrameStart());
		int frameEnd = getFrameEnd();
		animate(frameEnd - frameStart + 1);
		super.increaseFrameStart(frameStart - this.gameDraw.iFrame - 1);
	}
	
	public int getFrameStart()
	{
		int frameStart = Integer.MAX_VALUE;
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			frameStart = Math.min(frameStart, gameDrawOnFrames.frameStart);
		return frameStart;
	}
	
	public int getFrameEnd()
	{
		int frameEnd = 0;
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			frameEnd = Math.max(frameEnd, gameDrawOnFrames.frameEnd);
		return frameEnd;
	}
	
	@Override
	public GameDrawOnFrames increaseFrameStart(int framesBeforeStart)
	{
		super.increaseFrameStart(framesBeforeStart);
		for (GameDrawOnFrames draw : this.draws)
			draw.increaseFrameStart(framesBeforeStart);
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		for (GameDrawOnFrames gameDrawOnFrames : this.draws)
			gameDrawOnFrames.draw(canvas);
	}
	
}
