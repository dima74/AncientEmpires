package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.onframes.GameDrawOnFrames;

public class GameDrawInfoMove extends GameDrawOnFrames
{
	
	public void startShow()
	{
		int frameCount = 0;
		for (int y = main.gameDrawInfoY; y != 0; y /= 2)
			frameCount++;
		animate(frameCount);
		
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		main.gameDrawInfoY /= 2;
	}
	
}
