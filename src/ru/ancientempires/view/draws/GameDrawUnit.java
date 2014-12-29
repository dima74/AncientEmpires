package ru.ancientempires.view.draws;

import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.SomeWithBitmaps;
import ru.ancientempires.images.UnitBitmap;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;

public class GameDrawUnit extends GameDraw
{
	
	public final int		h;
	public final int		w;
	
	private UnitBitmap[][]	field;
	
	public GameDrawUnit(GameDrawMain gameDraw)
	{
		super(gameDraw);
		Unit[][] field = gameDraw.game.fieldUnits;
		this.h = field.length;
		this.w = field[0].length;
		this.field = new UnitBitmap[this.h][this.w];
	}
	
	@Override
	public boolean update(Game game)
	{
		Unit[][] field = game.fieldUnits;
		for (int i = 0; i < this.h; i++)
			for (int j = 0; j < this.w; j++)
				this.field[i][j] = getUnitBitmap(field[i][j]);
		
		return false;
	}
	
	private UnitBitmap getUnitBitmap(Unit unit)
	{
		if (unit == null)
			return null;
		SomeWithBitmaps baseBitmap = UnitImages.getUnitBitmap(unit, false);
		
		Bitmap textBitmap = Bitmap.createBitmap(GameDraw.A, GameDraw.A, Config.ARGB_4444);
		Canvas canvas = new Canvas(textBitmap);
		int health = unit.health;
		if (health < 100 && unit.isLive)
		{
			int textX = 0;
			int textY = GameDraw.A - NumberImages.h;
			
			final Bitmap bitmapOne = NumberImages.getNumberBitmap(health / 10);
			final Bitmap bitmapTwo = NumberImages.getNumberBitmap(health % 10);
			if (health / 10 == 0)
				canvas.drawBitmap(bitmapTwo, textX, textY, null);
			else
			{
				canvas.drawBitmap(bitmapOne, textX, textY, null);
				canvas.drawBitmap(bitmapTwo, textX + NumberImages.w, textY, null);
			}
		}
		return new UnitBitmap(baseBitmap, textBitmap);
	}
	
	public void updateOneUnit(int i, int j)
	{
		Unit[][] field = this.gameDraw.game.fieldUnits;
		this.field[i][j] = getUnitBitmap(field[i][j]);
	}
	
	public UnitBitmap extractUnit(int i, int j)
	{
		UnitBitmap unitBitmap = this.field[i][j];
		this.field[i][j] = null;
		return unitBitmap;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		for (int i = 0; i < this.h; i++)
			for (int j = 0; j < this.w; j++)
				drawUnit(canvas, this.field[i][j], i * GameDraw.A, j * GameDraw.A);
	}
	
	private void drawUnit(Canvas canvas, UnitBitmap unitBitmap, int y, int x)
	{
		if (unitBitmap == null)
			return;
		
		canvas.drawBitmap(unitBitmap.getBitmap(), x, y, null);
		canvas.drawBitmap(unitBitmap.textBitmap, x, y, null);
	}
	
}
