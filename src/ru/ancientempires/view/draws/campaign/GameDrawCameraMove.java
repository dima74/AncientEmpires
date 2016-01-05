package ru.ancientempires.view.draws.campaign;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesWithRangeFloat;

public class GameDrawCameraMove extends GameDrawOnFramesGroup
{
	
	public static float delta = GameDraw.a * 12;
	
	private float	startOffsetY;
	private float	startOffsetX;
	private float	endOffsetY;
	private float	endOffsetX;
	
	public void start(int iEnd, int jEnd)
	{
		startOffsetY = GameDraw.main.offsetY;
		startOffsetX = GameDraw.main.offsetX;
		
		endOffsetY = -iEnd * GameDraw.A - GameDraw.A / 2 + GameDraw.main.visibleMapH / GameDraw.mapScale / 2;
		endOffsetX = -jEnd * GameDraw.A - GameDraw.A / 2 + GameDraw.main.visibleMapW / GameDraw.mapScale / 2;
		endOffsetY = Math.max(GameDraw.main.minOffsetY, Math.min(GameDraw.main.maxOffsetY, endOffsetY));
		endOffsetX = Math.max(GameDraw.main.minOffsetX, Math.min(GameDraw.main.maxOffsetX, endOffsetX));
		
		float deltaY = endOffsetY - startOffsetY;
		float deltaX = endOffsetX - startOffsetX;
		int frameLength = Math.round(Math.max(Math.abs(deltaY), Math.abs(deltaX)) * GameDraw.mapScale / GameDrawCameraMove.delta);
		float stepY = deltaY == 0 ? 1 : deltaY / frameLength;
		float stepX = deltaX == 0 ? 1 : deltaX / frameLength;
		
		add(new GameDrawOnFramesWithRangeFloat()
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				synchronized (GameDraw.main)
				{
					GameDraw.main.nextOffsetY = value;
				}
			}
		}.animateRange(startOffsetY, endOffsetY, stepY));
		add(new GameDrawOnFramesWithRangeFloat()
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				synchronized (GameDraw.main)
				{
					GameDraw.main.nextOffsetX = value;
				}
			}
		}.animateRange(startOffsetX, endOffsetX, stepX));
		
		GameDraw.main.inputPlayer.tapWithoutAction(iEnd, jEnd);
	}
	
}
