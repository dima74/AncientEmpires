package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.Images;

public class GameDrawUnitsDead extends GameDraw
{
	
	public boolean[][]	isTombstone	= new boolean[GameHandler.h][GameHandler.w];
	public boolean[][]	keep		= new boolean[GameHandler.h][GameHandler.w];
	
	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < GameHandler.h; i++)
			for (int j = 0; j < GameHandler.w; j++)
			{
				if (!keep[i][j])
					isTombstone[i][j] = GameHandler.fieldDeadUnits[i][j] != null;
				if (isTombstone[i][j])
					canvas.drawBitmap(Images.tombstone.getBitmap(), j * GameDraw.A, i * GameDraw.A, null);
			}
	}
	
}
