package ru.ancientempires.draws;

import android.graphics.Canvas;

public class DrawUnitsDead extends Draw
{
	
	public boolean[][] isTombstone = new boolean[game.h][game.w];
	public boolean[][] keep        = new boolean[game.h][game.w];
	
	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
			{
				if (!keep[i][j])
					isTombstone[i][j] = game.fieldUnitsDead[i][j] != null;
				if (isTombstone[i][j])
					canvas.drawBitmap(Images().tombstone.getBitmap(), j * A, i * A, null);
			}
	}
	
}
