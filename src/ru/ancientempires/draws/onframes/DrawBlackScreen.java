package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

public class DrawBlackScreen extends DrawOnFramesWithRangeValues
{
	
	public void startShow()
	{
		animateRange(15, 255, 16);
	}
	
	public void startHide()
	{
		main.isBlackScreen = false;
		animateRange(240, 0, 16);
	}
	
	@Override
	public void draw(Canvas canvas, int alpha)
	{
		canvas.drawColor(alpha << 24);
	}
	
	@Override
	public void onEndDraw()
	{
		if (end == 255)
			main.isBlackScreen = true;
	}
	
}
