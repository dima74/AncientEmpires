package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameDrawInfoNull extends GameDraw
{
	
	public int	a	= 2;
	public int	h	= GameDrawInfo.mA + 8 * 2;
	public int	w	= super.w;
	
	private Bitmap backgroundBitmap;
	
	public GameDrawInfoNull()
	{
		backgroundBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(backgroundBitmap);
		drawRect(canvas, 0, 0, w, h, GameDrawInfo.color4);
		drawRect(canvas, 0, 0, w, a, GameDrawInfo.color3);
		drawRect(canvas, 0, a, w, a * 2, GameDrawInfo.color1);
		drawRect(canvas, 0, a * 3, w, a, GameDrawInfo.color2);
		drawRect(canvas, 0, a * 4, w, a, GameDrawInfo.color5);
		
		// 3 1
		// 1 2
		// 2 1
		// 5 1
		// 4 h
	}
	
	public void drawRect(Canvas canvas, int x, int y, int w, int h, Paint paint)
	{
		canvas.drawRect(x, y, x + w, y + h, paint);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(backgroundBitmap, 0, 0, null);
	}
	
}
