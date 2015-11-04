package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawBlackScreen extends GameDrawOnFramesWithRangeValues
{
	
	public GameDrawBlackScreen(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void startShow()
	{
		animateRange(15, 255, 16);
	}
	
	public void startHide()
	{
		animateRange(240, 0, 16);
	}
	
	@Override
	public void draw(Canvas canvas, int alpha)
	{
		canvas.drawColor(alpha << 24);
	}
	
}
