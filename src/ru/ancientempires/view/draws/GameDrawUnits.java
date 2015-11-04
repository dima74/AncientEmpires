package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Unit;

public class GameDrawUnits extends GameDraw
{
	
	public UnitBitmap[][]	field;
	public boolean[][]		keep;
	
	public GameDrawUnits(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.field = new UnitBitmap[GameHandler.h][GameHandler.w];
		this.keep = new boolean[GameHandler.h][GameHandler.w];
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		Unit floatingUnit = this.gameDraw.game.floatingUnit;
		if (floatingUnit != null)
			GameDrawUnits.drawUnit(canvas, new UnitBitmap(floatingUnit));
		for (int i = 0; i < GameHandler.h; i++)
			for (int j = 0; j < GameHandler.w; j++)
			{
				if (i == 4 && j == 7)
					System.out.println();
				if (!this.keep[i][j])
					if (GameHandler.fieldUnits[i][j] == null)
						this.field[i][j] = null;
					else if (this.field[i][j] == null || this.field[i][j].unit != GameHandler.fieldUnits[i][j])
						this.field[i][j] = new UnitBitmap(GameHandler.fieldUnits[i][j]);
				if (this.field[i][j] != null)
					GameDrawUnits.drawUnit(canvas, this.field[i][j]);
			}
	}
	
	public static void drawUnit(Canvas canvas, UnitBitmap unitBitmap)
	{
		canvas.drawBitmap(unitBitmap.getBaseBitmap(), unitBitmap.x, unitBitmap.y, null);
		Bitmap healthBitmap = unitBitmap.getHealthBitmap();
		if (healthBitmap != null)
			canvas.drawBitmap(unitBitmap.getHealthBitmap(), unitBitmap.x, unitBitmap.y + GameDraw.A - SmallNumberImages.images.h, null);
	}
	
}
