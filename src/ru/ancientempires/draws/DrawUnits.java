package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Unit;

public class DrawUnits extends Draw
{
	
	public UnitBitmap[][]	field	= new UnitBitmap[game.h][game.w];
	public boolean[][]		keep	= new boolean[game.h][game.w];
	public boolean[][]		move	= new boolean[game.h][game.w];
									
	public void update()
	{
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				updateUnit(i, j);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Unit floatingUnit = game.floatingUnit;
		if (floatingUnit != null)
			drawUnit(canvas, new UnitBitmap(floatingUnit));
			
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
				if (!move[i][j])
				{
					updateUnit(i, j);
					if (field[i][j] != null)
						drawUnit(canvas, field[i][j]);
				}
	}
	
	public void updateUnit(int i, int j)
	{
		if (!keep[i][j])
			if (game.fieldUnits[i][j] == null)
				field[i][j] = null;
			else if (field[i][j] == null || field[i][j].unit != game.fieldUnits[i][j])
				field[i][j] = new UnitBitmap(game.fieldUnits[i][j]);
	}
	
	public void drawUnit(Canvas canvas, UnitBitmap unitBitmap)
	{
		canvas.drawBitmap(unitBitmap.getBaseBitmap(), unitBitmap.x, unitBitmap.y, null);
		Bitmap healthBitmap = unitBitmap.getHealthBitmap();
		if (healthBitmap != null)
			canvas.drawBitmap(unitBitmap.getHealthBitmap(), unitBitmap.x, unitBitmap.y + A - SmallNumberImages().h, null);
	}
	
}
