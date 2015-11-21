package ru.ancientempires.view.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmap extends GameDrawOnFrames
{
	
	private Bitmap	bitmap;
	private int		y;
	private int		x;
	
	public GameDrawBitmap setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		return this;
	}
	
	public GameDrawBitmap setYX(int y, int x)
	{
		this.y = y;
		this.x = x;
		return this;
	}
	
	@Override
	public void drawOnFrames(Canvas canvas)
	{
		canvas.drawBitmap(bitmap, x, y, null);
	}
	
}
