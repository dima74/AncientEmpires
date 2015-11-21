package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import ru.ancientempires.GameView;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.model.Cell;

public class GameDrawInfo extends GameDraw
{
	
	public static float			mScale	= 2.0f;
	public static int			mA		= (int) (Images.bitmapSize * GameDrawInfo.mScale);
	private static final Paint	paint	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color1	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color2	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color3	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color4	= new Paint(Paint.ANTI_ALIAS_FLAG);
	private static final Paint	color5	= new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public boolean	isActive	= true;
	private Bitmap	backgroundBitmap;
	
	public int	a;
	public int	h;
	public int	w;
	private int	mW;
	private int	color;
	
	private Bitmap	goldBitmap;
	private Bitmap	amountBitmap;
	
	public GameDrawInfo()
	{
		w = GameView.w;
		h = GameDrawInfo.mA + 8 * 2;
		
		backgroundBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(backgroundBitmap);
		
		GameDrawInfo.color1.setColor(0xFFAFB7AB);
		GameDrawInfo.color2.setColor(0xFF6D7581);
		GameDrawInfo.color3.setColor(0xFF434A64);
		GameDrawInfo.color4.setColor(0xFF242A45);
		GameDrawInfo.color5.setColor(0xFF12142F);
		
		a = 2;// GameDraw.A / 24;
		mW = w - a * 7 - GameDrawInfo.mA;
		
		GameDrawInfo.paint.setStrokeWidth(1);
		GameDrawInfo.color1.setStrokeWidth(a);
		GameDrawInfo.color2.setStrokeWidth(a);
		GameDrawInfo.color3.setStrokeWidth(a);
		GameDrawInfo.color4.setStrokeWidth(a);
		GameDrawInfo.color5.setStrokeWidth(a);
		
		drawLeftPart(canvas, h, mW);
		drawRightPart(canvas, h, w, mW - a);
	}
	
	private void drawLeftPart(Canvas canvas, float h, float w)
	{
		float nh = h / a;
		float nw = w / a;
		canvas.drawRect(0, 0, w, h, GameDrawInfo.color3);
		canvas.drawRect(a, a, w - a, h - a, GameDrawInfo.color1);
		canvas.drawRect(3 * a, 3 * a, w - 3 * a, h - 3 * a, GameDrawInfo.color3);
		
		drawLine(canvas, 3, 3.5f, 6, 3.5f, GameDrawInfo.color5, true, true, true, nh, nw);
		drawLine(canvas, 4, 4.5f, 6, 4.5f, GameDrawInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 4, 5.5f, 6, 5.5f, GameDrawInfo.color4, false, true, true, nh, nw);
		drawLine(canvas, 3, 6.5f, 7, 6.5f, GameDrawInfo.color1, true, true, true, nh, nw);
		drawLine(canvas, 7.5f, 4, 7.5f, 8, GameDrawInfo.color2, false, false, true, nh, nw);
		drawLine(canvas, 3.5f, 8, 3.5f, nw - 8, GameDrawInfo.color2, false, false, false, nh, nw);
		drawLine(canvas, 8, 4.5f, nh - 8, 4.5f, GameDrawInfo.color5, false, false, true, nh, nw);
		drawLine(canvas, 4.5f, 8, 4.5f, nw - 8, GameDrawInfo.color5, false, true, false, nh, nw);
		
		canvas.drawRect(5 * a, 8 * a, w - 5 * a, h - 8 * a, GameDrawInfo.color4);
		canvas.drawRect(8 * a, 5 * a, w - 8 * a, h - 5 * a, GameDrawInfo.color4);
	}
	
	private void drawLine(Canvas canvas, float i1, float j1, float i2, float j2, Paint paint,
			boolean axial, boolean vertical, boolean horizontal, float nh, float nw)
	{
		canvas.drawLine(a * j1, a * i1, a * j2, a * i2, paint);
		if (axial)
			drawLine(canvas, j1, i1, j2, i2, paint, false, vertical, horizontal, nh, nw);
		if (vertical)
			drawLine(canvas, nh - i1, j1, nh - i2, j2, paint, false, false, horizontal, nh, nw);
		if (horizontal)
			drawLine(canvas, i1, nw - j1, i2, nw - j2, paint, false, false, false, nh, nw);
	}
	
	private void drawRightPart(Canvas canvas, float h, float w, float mW)
	{
		canvas.drawRect(mW, 0, w, h, GameDrawInfo.color3);
		canvas.drawRect(mW + a, a, w - a, h - a, GameDrawInfo.color1);
		float a3 = a * 3;
		canvas.drawRect(mW + a3, a3, w - a3, h - a3, GameDrawInfo.color3);
	}
	
	@Override
	public boolean update()
	{
		color = GameDraw.game.currentPlayer.color.showColor;
		goldBitmap = BigNumberImages.images.createBitmap(GameDraw.game.currentPlayer.gold);
		amountBitmap = BigNumberImages.images.createBitmap(GameDraw.game.currentPlayer.units.size());
		return false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (!isActive)
		{
			drawLeftPart(canvas, h, w);
			return;
		}
		
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		
		if (true)
		{
			Cell cell = GameHandler.fieldCells[GameHandler.game.currentPlayer.cursorI][GameHandler.game.currentPlayer.cursorJ];
			// изображение клеточки
			canvas.save();
			int bitmapY = a * 4;
			int bitmapX = mW + a * 3;
			canvas.scale(GameDrawInfo.mScale, GameDrawInfo.mScale, bitmapX, bitmapY);
			canvas.drawBitmap(CellImages.getCellBitmap(cell, false), bitmapX, bitmapY, null);
			
			// и ее защиты
			Bitmap defenceNumberBitmap = SmallNumberImages.images.getBitmap(cell.type.defense);
			int y = bitmapY + GameDrawInfo.mA - defenceNumberBitmap.getHeight();
			int x = bitmapX + GameDrawInfo.mA - defenceNumberBitmap.getWidth();
			canvas.drawBitmap(defenceNumberBitmap, x, y, null);
			
			y -= SmallNumberImages.defenceBitmap.getHeight();
			x -= SmallNumberImages.defenceBitmap.getWidth();
			canvas.drawBitmap(SmallNumberImages.defenceBitmap, x, y, null);
			canvas.restore();
		}
		
		// градиент по цвету игрока
		int amn = 8;
		for (int i = 0; i < amn; i++)
		{
			GameDrawInfo.paint.setColor(color & 0x00FFFFFF | 0xFF * (amn - i) / amn << 24);
			
			float y1 = (5 + i) * a;
			float x1 = (i < 3 ? 8 : 5) * a;
			float y2 = (5 + i) * a;
			float x2 = mW - (i < 3 ? 8 : 5) * a - .5f;
			for (int k = 0; k < a; k++)
				canvas.drawLine(x1, y1 + k + 1, x2, y2 + k + 1, GameDrawInfo.paint);
		}
		
		int xGold = (int) (mW * .075f);
		int xUnits = (int) (mW * .5f);
		float yGold = (h - Images.amountGoldH * GameDrawInfo.mScale) / 2;
		float yUnits = (h - Images.amountUnitsH * GameDrawInfo.mScale) / 2;
		
		canvas.save();
		canvas.scale(GameDrawInfo.mScale, GameDrawInfo.mScale, xGold, yGold);
		canvas.drawBitmap(Images.amountGold, xGold, yGold, null);
		canvas.drawBitmap(goldBitmap, xGold + Images.amountGoldW + a, yGold, null);
		canvas.restore();
		
		canvas.save();
		canvas.scale(GameDrawInfo.mScale, GameDrawInfo.mScale, xUnits, yUnits);
		canvas.drawBitmap(Images.amountUnits, xUnits, yUnits, null);
		canvas.drawBitmap(amountBitmap, xUnits + Images.amountUnitsW + a, yUnits, null);
		canvas.restore();
	}
	
}
