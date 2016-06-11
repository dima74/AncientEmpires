package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

public class DrawSnakeMap extends DrawOnFrames
{

	@Override
	public void drawOnFrames(Canvas canvas)
	{
		if (iFrame() % 2 == 0)
		{
			main.extraOffsetY = main.rnd.nextInt() % 4;
			main.extraOffsetX = main.rnd.nextInt() % 10;
		}
	}

	@Override
	public void onEnd()
	{
		main.extraOffsetY = 0;
		main.extraOffsetX = 0;
	}

}
