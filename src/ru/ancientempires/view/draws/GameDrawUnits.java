package ru.ancientempires.view.draws;

import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Unit;

public class GameDrawUnits extends GameDraw
{
	
	public UnitBitmap[][]	field	= new UnitBitmap[game.h][game.w];
	public boolean[][]		keep	= new boolean[game.h][game.w];
	
	public HashSet<UnitBitmap> moveUnits = new HashSet<UnitBitmap>();
	
	@Override
	public void draw(Canvas canvas)
	{
		Unit floatingUnit = game.floatingUnit;
		if (floatingUnit != null)
			drawUnit(canvas, new UnitBitmap(floatingUnit));
			
		// UnitBitmap specialBitmap = null;
		for (int i = 0; i < game.h; i++)
			for (int j = 0; j < game.w; j++)
			{
				updateUnit(i, j);
				if (field[i][j] != null)
					drawUnit(canvas, field[i][j]);
				// if (field[i][j].y != i * A || field[i][j].x != j * A)
				// specialBitmap = field[i][j];
			}
		// if (specialBitmap != null)
		// drawUnit(canvas, specialBitmap);
		for (UnitBitmap unitBitmap : moveUnits)
			drawUnit(canvas, unitBitmap);
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
