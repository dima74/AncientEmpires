package ru.ancientempires.view.draws;

import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.SmallNumberImages;
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
				this.field[i][j] = getUnitBitmap(field[i][j]);
		
		return false;
	}
	
	private UnitBitmap getUnitBitmap(Unit unit)
	{
		if (unit == null)
			return null;
		FewBitmaps baseBitmap = UnitImages.getUnitBitmap(unit, false);
		Bitmap textBitmap = Bitmap.createBitmap(GameDraw.A, GameDraw.A, Config.ARGB_8888);
		UnitBitmap unitBitmap = new UnitBitmap(baseBitmap, textBitmap, unit);
		if (unit.isLive)
			updateTextBitmap(textBitmap, unit.health);
		return unitBitmap;
	}
	
	private void updateTextBitmap(Bitmap textBitmap, int health)
	{
		textBitmap.eraseColor(Color.TRANSPARENT);
		Canvas canvas = new Canvas(textBitmap);
		if (health < 100)
		{
			NumberImages images = SmallNumberImages.images;
			int textX = 0;
			int textY = GameDraw.A - images.h;
			
			final Bitmap bitmapOne = images.getBitmap(health / 10);
			final Bitmap bitmapTwo = images.getBitmap(health % 10);
			if (health / 10 == 0)
				canvas.drawBitmap(bitmapTwo, textX, textY, null);
			else
			{
				canvas.drawBitmap(bitmapOne, textX, textY, null);
				canvas.drawBitmap(bitmapTwo, textX + images.w, textY, null);
			}
		}
	}
	
	public void updateOneUnitBase(int i, int j, boolean isLive)
	{
		UnitBitmap unitBitmap = this.field[i][j];
		FewBitmaps fewBitmap = UnitImages.getUnitBitmap(unitBitmap.unit, isLive);
		this.field[i][j] = new UnitBitmap(fewBitmap, unitBitmap.textBitmap, unitBitmap.unit);
	}
	
	public void updateOneUnitHealth(int i, int j, boolean isLive)
	{
		UnitBitmap unitBitmap = this.field[i][j];
		updateTextBitmap(unitBitmap.textBitmap, unitBitmap.unit.health);
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
