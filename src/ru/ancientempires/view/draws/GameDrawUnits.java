package ru.ancientempires.view.draws;

import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Unit;

public class GameDrawUnits extends GameDraw
{
	
	public UnitBitmap[][]	field	= new UnitBitmap[GameHandler.h][GameHandler.w];
	public boolean[][]		keep	= new boolean[GameHandler.h][GameHandler.w];
	
	public HashSet<UnitBitmap> moveUnits = new HashSet<UnitBitmap>();
	
	@Override
	public void draw(Canvas canvas)
	{
		Unit floatingUnit = GameDraw.game.floatingUnit;
		if (floatingUnit != null)
			GameDrawUnits.drawUnit(canvas, new UnitBitmap(floatingUnit));
			
		// UnitBitmap specialBitmap = null;
		for (int i = 0; i < GameHandler.h; i++)
			for (int j = 0; j < GameHandler.w; j++)
			{
				updateUnit(i, j);
				if (field[i][j] != null)
					GameDrawUnits.drawUnit(canvas, field[i][j]);
				// if (field[i][j].y != i * GameDraw.A || field[i][j].x != j * GameDraw.A)
				// specialBitmap = field[i][j];
			}
		// if (specialBitmap != null)
		// GameDrawUnits.drawUnit(canvas, specialBitmap);
		for (UnitBitmap unitBitmap : moveUnits)
			GameDrawUnits.drawUnit(canvas, unitBitmap);
	}
	
	public void updateUnit(int i, int j)
	{
		if (!keep[i][j])
			if (GameHandler.fieldUnits[i][j] == null)
				field[i][j] = null;
			else if (field[i][j] == null || field[i][j].unit != GameHandler.fieldUnits[i][j])
				field[i][j] = new UnitBitmap(GameHandler.fieldUnits[i][j]);
	}
	
	public static void drawUnit(Canvas canvas, UnitBitmap unitBitmap)
	{
		canvas.drawBitmap(unitBitmap.getBaseBitmap(), unitBitmap.x, unitBitmap.y, null);
		Bitmap healthBitmap = unitBitmap.getHealthBitmap();
		if (healthBitmap != null)
			canvas.drawBitmap(unitBitmap.getHealthBitmap(), unitBitmap.x, unitBitmap.y + GameDraw.A - SmallNumberImages.images.h, null);
	}
	
}
