package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmapSinus extends GameDrawSinus
{
	
	private Bitmap	bitmap;
	
	public GameDrawBitmapSinus(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawBitmapSinus setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
		return this;
	}
	
	@Override
	public void drawChild(Canvas canvas, int i)
	{
		canvas.drawBitmap(this.bitmap, 0, 0, null);
	}
	
}
