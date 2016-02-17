package ru.ancientempires.draws.campaign;

import android.graphics.Canvas;
import ru.ancientempires.draws.Draw;
import ru.ancientempires.draws.onframes.DrawOnFramesGroup;
import ru.ancientempires.draws.onframes.DrawOnFramesWithRangeFloat;

public class DrawCameraMove extends DrawOnFramesGroup
{
	
	public static float delta = new Draw().a * 12;
	
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
		int frameLength = Math.round(Math.max(Math.abs(deltaY), Math.abs(deltaX)) * mapScale / DrawCameraMove.delta);
		float stepY = deltaY == 0 ? 1 : deltaY / frameLength;
		float stepX = deltaX == 0 ? 1 : deltaX / frameLength;
		
		add(new DrawOnFramesWithRangeFloat()
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
		add(new DrawOnFramesWithRangeFloat()
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
