package ru.ancientempires.view.draws;

import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;

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
				if (field[i][j] != null)
					this.field[i][j] = getUnitBitmap(field[i][j]);
		
		return false;
	}
	
	private UnitBitmap getUnitBitmap(Unit unit)
	{
		FewBitmaps baseBitmap = UnitImages.getUnitBitmap(unit, false);
		Bitmap textBitmap = Bitmap.createBitmap(GameDraw.A, GameDraw.A, Config.ARGB_8888);
		UnitBitmap unitBitmap = new UnitBitmap(baseBitmap, textBitmap);
		updateTextBitmap(textBitmap, unit);
		return unitBitmap;
	}
	
	private void updateTextBitmap(Bitmap textBitmap, Unit unit)
	{
		textBitmap.eraseColor(Color.TRANSPARENT);
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
	}
	
	public void updateOneUnitBase(int i, int j, boolean isLive)
	{
		Unit[][] field = this.gameDraw.game.fieldUnits;
		FewBitmaps unitBitmap = UnitImages.getUnitBitmap(field[i][j], isLive);
		this.field[i][j] = new UnitBitmap(unitBitmap, this.field[i][j].textBitmap);
	}
	
	public void updateOneUnitHealth(int i, int j)
	{
		Unit[][] field = this.gameDraw.game.fieldUnits;
		updateTextBitmap(this.field[i][j].textBitmap, field[i][j]);
	}
	
	public void updateOneUnit(int i, int j)
	{
		this.field[i][j] = getUnitBitmap(this.gameDraw.game.fieldUnits[i][j]);
	}
	
	public void updateOneUnit(Point point)
	{
		updateOneUnit(point.i, point.j);
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
