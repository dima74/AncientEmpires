package ru.ancientempires.draws.campaign;

import android.graphics.Canvas;

import ru.ancientempires.draws.onframes.DrawOnFramesGroup;
import ru.ancientempires.draws.onframes.DrawOnFramesWithRangeFloat;

public class DrawCameraMove extends DrawOnFramesGroup
{
	
	public static float delta = 6;

	public void start(int iEnd, int jEnd)
	{
		float startOffsetY = main.offsetY;
		float startOffsetX = main.offsetX;
		
		float endOffsetY = -iEnd * A - A / 2 + main.visibleMapH / mapScale / 2;
		float endOffsetX = -jEnd * A - A / 2 + main.visibleMapW / mapScale / 2;
		endOffsetY = Math.max(main.minOffsetY, Math.min(main.maxOffsetY, endOffsetY));
		endOffsetX = Math.max(main.minOffsetX, Math.min(main.maxOffsetX, endOffsetX));
		
		float deltaY = endOffsetY - startOffsetY;
		float deltaX = endOffsetX - startOffsetX;
		int frameLength = Math.round(Math.max(Math.abs(deltaY), Math.abs(deltaX)) / delta);
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
