package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import java.util.ArrayList;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawOnFramesGroup extends DrawOnFrames
{

	public ArrayList<DrawOnFrames> draws = new ArrayList<>();

	public DrawOnFramesGroup(BaseDrawMain mainBase)
	{
		super(mainBase);
	}

	public void add(DrawOnFrames gameDraw)
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
		for (DrawOnFrames gameDrawOnFrames : draws)
			frameStart = Math.min(frameStart, gameDrawOnFrames.frameStart);
		return frameStart;
	}

	public int getFrameEnd()
	{
		int frameEnd = 0;
		for (DrawOnFrames gameDrawOnFrames : draws)
			frameEnd = Math.max(frameEnd, gameDrawOnFrames.frameEnd);
		return frameEnd;
	}

	@Override
	public DrawOnFrames increaseFrameStart(int framesBeforeStart)
	{
		super.increaseFrameStart(framesBeforeStart);
		for (DrawOnFrames draw : draws)
			draw.increaseFrameStart(framesBeforeStart);
		return this;
	}

	@Override
	public void drawOnFrames(Canvas canvas)
	{
		for (DrawOnFrames gameDrawOnFrames : draws)
			gameDrawOnFrames.draw(canvas);
	}

}
