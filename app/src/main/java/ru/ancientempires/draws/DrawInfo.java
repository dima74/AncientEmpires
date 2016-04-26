package ru.ancientempires.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import ru.ancientempires.images.Images;
import ru.ancientempires.model.Cell;

public class DrawInfo extends Draw
{
	
	public static float			mScale	= 2.0f;
	public static int			mA		= (int) (Images.get().bitmapSize * DrawInfo.mScale);
	public static final Paint	paint	= new Paint();
	public static final Paint	color1	= new Paint();
	public static final Paint	color2	= new Paint();
	public static final Paint	color3	= new Paint();
	public static final Paint	color4	= new Paint();
	public static final Paint	color5	= new Paint();
										
	static
	{
		DrawInfo.color1.setColor(0xFFAFB7AB);
		DrawInfo.color2.setColor(0xFF6D7581);
		DrawInfo.color3.setColor(0xFF434A64);
		DrawInfo.color4.setColor(0xFF242A45);
		DrawInfo.color5.setColor(0xFF12142F);
	}
	
	private Bitmap	backgroundBitmap;
					
	public int		a	= 2;
	public int		h	= DrawInfo.mA + 8 * 2;
	public int		w	= w();
	private int		mW	= w - a * 7 - DrawInfo.mA;
	private int		color;
					
	private Bitmap	goldBitmap;
	private Bitmap	amountBitmap;
					
	public DrawInfo()
	{
		backgroundBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(backgroundBitmap);
		drawLeftPart(canvas, h, mW);
		drawRightPart(canvas, h, w, mW - a);
	}
	
	private void drawLeftPart(Canvas canvas, int h, int w)
	{
		// float nh = h / a;
		// float nw = w / a;
		canvas.drawRect(0, 0, w, h, DrawInfo.color3);
		canvas.drawRect(a, a, w - a, h - a, DrawInfo.color1);
		canvas.drawRect(3 * a, 3 * a, w - 3 * a, h - 3 * a, DrawInfo.color4);
		
		/*
			
			0123456789
			
		0	33333333333
		1	31111111111
		2	31111111111
		3	31155513222
		4	31154413555
		5	31154413
		6	31111113
		7	31132222
		8	31135     4
		9	31135    44
			31135   444
			
		*/
		
		drawRect(canvas, 3, 3, 1, 3, color5, true, true, true, true, h, w);
		drawRect(canvas, 3, 6, 4, 1, color1, true, true, true, true, h, w);
		drawRect(canvas, 3, 7, 5, 1, color3, true, true, true, true, h, w);
		drawRect(canvas, 7, 4, 1, 4, color2, false, false, true, true, h, w);
		
		drawRect(canvas, 3 * a, 8 * a, a, w - 16 * a, color2, false, false, false, false, h, w);
		drawRect(canvas, 4 * a, 8 * a, a, w - 16 * a, color5, false, true, false, false, h, w);
		drawRect(canvas, 8 * a, 4 * a, h - 16 * a, a, color5, false, false, true, false, h, w);
		drawRect(canvas, 7 * a, 3 * a, h - 14 * a, a, color3, false, false, true, false, h, w);
	}
	
	private void drawRect(Canvas canvas, int y, int x, int h, int w, Paint paint,
			boolean axial, boolean vertical, boolean horizontal, boolean needMulti, int nh, int nw)
	{
		if (needMulti)
			drawRect(canvas, y * a, x * a, h * a, w * a, paint, axial, vertical, horizontal, false, nh, nw);
		else
		{
			canvas.drawRect(x, y, x + w, y + h, paint);
			if (axial)
				drawRect(canvas, x, y, w, h, paint, false, vertical, horizontal, false, nh, nw);
			if (vertical)
				drawRect(canvas, nh - y - h, x, h, w, paint, false, false, horizontal, false, nh, nw);
			if (horizontal)
				drawRect(canvas, y, nw - x - w, h, w, paint, false, false, false, false, nh, nw);
		}
	}
	
	private void drawRightPart(Canvas canvas, float h, float w, float mW)
	{
		canvas.drawRect(mW, 0, w, h, DrawInfo.color3);
		canvas.drawRect(mW + a, a, w - a, h - a, DrawInfo.color1);
		float a3 = a * 3;
		canvas.drawRect(mW + a3, a3, w - a3, h - a3, DrawInfo.color3);
	}
	
	public void update()
	{
		color = game.currentPlayer.color.showColor;
		goldBitmap = BigNumberImages().createBitmap(game.currentPlayer.gold);
		amountBitmap = BigNumberImages().createBitmap(game.currentPlayer.units.size());
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
		
		if (true)
		{
			Cell cell = game.fieldCells[game.currentPlayer.cursorI][game.currentPlayer.cursorJ];
			// изображение клеточки
			canvas.save();
			int bitmapY = a * 4;
			int bitmapX = mW + a * 3;
			canvas.scale(DrawInfo.mScale, DrawInfo.mScale, bitmapX, bitmapY);
			canvas.drawBitmap(CellImages().getCellBitmap(cell, false).getBitmap(), bitmapX, bitmapY, null);
			
			// и ее защиты
			Bitmap defenceNumberBitmap = SmallNumberImages().getBitmap(cell.type.defense);
			int y = bitmapY + DrawInfo.mA - defenceNumberBitmap.getHeight();
			int x = bitmapX + DrawInfo.mA - defenceNumberBitmap.getWidth();
			canvas.drawBitmap(defenceNumberBitmap, x, y, null);
			
			y -= SmallNumberImages().defenceBitmap.getHeight();
			x -= SmallNumberImages().defenceBitmap.getWidth();
			canvas.drawBitmap(SmallNumberImages().defenceBitmap, x, y, null);
			canvas.restore();
		}
		
		// градиент по цвету игрока
		int number = 8;
		for (int i = 0; i < number; i++)
		{
			DrawInfo.paint.setColor(color & 0x00FFFFFF | 0xFF * (number - i) / number << 24);
			
			int y1 = (5 + i) * a;
			int x1 = (i < 3 ? 8 : 5) * a;
			int y2 = y1 + a;
			int x2 = mW - x1;
			canvas.drawRect(x1, y1, x2, y2, DrawInfo.paint);
		}
		
		int xGold = (int) (mW * .075f);
		int xUnits = (int) (mW * .5f);
		float yGold = (h - Images().amountGoldH * DrawInfo.mScale) / 2;
		float yUnits = (h - Images().amountUnitsH * DrawInfo.mScale) / 2;
		
		canvas.save();
		canvas.scale(DrawInfo.mScale, DrawInfo.mScale, xGold, yGold);
		canvas.drawBitmap(Images().amountGold, xGold, yGold, null);
		canvas.drawBitmap(goldBitmap, xGold + Images().amountGoldW + a, yGold, null);
		canvas.restore();
		
		canvas.save();
		canvas.scale(DrawInfo.mScale, DrawInfo.mScale, xUnits, yUnits);
		canvas.drawBitmap(Images().amountUnits, xUnits, yUnits, null);
		canvas.drawBitmap(amountBitmap, xUnits + Images().amountUnitsW + a, yUnits, null);
		canvas.restore();
	}
	
}
