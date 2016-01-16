package ru.ancientempires.view.draws.campaign;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesGroup;
import ru.ancientempires.view.draws.onframes.GameDrawOnFramesWithRangeFloat;

public class GameDrawCameraMove extends GameDrawOnFramesGroup
{
	
	public static float delta = new GameDraw().a * 12;
	
	private float	startOffsetY;
	private float	startOffsetX;
	private float	endOffsetY;
	private float	endOffsetX;
	
	public void start(int iEnd, int jEnd)
	{
		startOffsetY = main.offsetY;
		startOffsetX = main.offsetX;
		
		endOffsetY = -iEnd * A - A / 2 + main.visibleMapH / mapScale / 2;
		endOffsetX = -jEnd * A - A / 2 + main.visibleMapW / mapScale / 2;
		endOffsetY = Math.max(main.minOffsetY, Math.min(main.maxOffsetY, endOffsetY));
		endOffsetX = Math.max(main.minOffsetX, Math.min(main.maxOffsetX, endOffsetX));
		
		float deltaY = endOffsetY - startOffsetY;
		float deltaX = endOffsetX - startOffsetX;
		int frameLength = Math.round(Math.max(Math.abs(deltaY), Math.abs(deltaX)) * mapScale / GameDrawCameraMove.delta);
		float stepY = deltaY == 0 ? 1 : deltaY / frameLength;
		float stepX = deltaX == 0 ? 1 : deltaX / frameLength;
		
		add(new GameDrawOnFramesWithRangeFloat()
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				synchronized (main)
				{
					main.nextOffsetY = value;
				}
			}
		}.animateRange(startOffsetY, endOffsetY, stepY));
		add(new GameDrawOnFramesWithRangeFloat()
		{
			@Override
			public void draw(Canvas canvas, float value)
			{
				synchronized (main)
				{
					main.nextOffsetX = value;
				}
			}
		}.animateRange(startOffsetX, endOffsetX, stepX));
		
		main.inputPlayer.tapWithoutAction(iEnd, jEnd);
	}
	
}
