package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawBlackScreen extends GameDrawOnFramesWithRangeValues
{
	
	public void startShow()
	{
		animateRange(15, 255, 16);
	}
	
	public void startHide()
	{
		GameDraw.main.isBlackScreen = false;
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
			GameDraw.main.isBlackScreen = true;
	}
	
}
