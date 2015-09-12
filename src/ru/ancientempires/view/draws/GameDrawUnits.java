package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;

public class GameDrawUnits extends GameDraw
{
	
	public final int	h;
	public final int	w;
	
	private UnitBitmap[][] field;
	
	public GameDrawUnits(GameDrawMain gameDraw)
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
				this.field[i][j] = GameDrawUnits.getUnitBitmap(field[i][j]);
				
		return false;
	}
	
	public static UnitBitmap getUnitBitmap(Unit unit)
	{
		if (unit == null)
			return null;
		FewBitmaps baseBitmap = UnitImages.getUnitBitmap(unit);
		Bitmap textBitmap = Bitmap.createBitmap(GameDraw.A, GameDraw.A, Config.ARGB_8888);
		GameDrawUnits.updateTextBitmap(textBitmap, unit.health);
		return new UnitBitmap(baseBitmap, textBitmap, unit);
	}
	
	private static void updateTextBitmap(Bitmap textBitmap, int health)
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
		FewBitmaps fewBitmap = UnitImages.getUnitBitmap(unitBitmap.unit);
		this.field[i][j] = new UnitBitmap(fewBitmap, unitBitmap.textBitmap, unitBitmap.unit);
	}
	
	public void updateOneUnitHealth(int i, int j, boolean isLive)
	{
		UnitBitmap unitBitmap = this.field[i][j];
		GameDrawUnits.updateTextBitmap(unitBitmap.textBitmap, unitBitmap.unit.health);
	}
	
	public void updateOneUnit(int i, int j)
	{
		if (GameHandler.checkCoord(i, j))
			this.field[i][j] = GameDrawUnits.getUnitBitmap(this.gameDraw.game.fieldUnits[i][j]);
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
		Unit floatingUnit = this.gameDraw.game.floatingUnit;
		if (floatingUnit != null)
			drawUnit(canvas, GameDrawUnits.getUnitBitmap(floatingUnit), floatingUnit.i * GameDraw.A, floatingUnit.j * GameDraw.A);
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
