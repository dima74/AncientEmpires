package ru.ancientempires.view.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawBitmap extends GameDrawOnFrames
{
	
	private Bitmap	bitmap;
	private int		y;
	private int		x;
	
	public GameDrawBitmap(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
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
		canvas.drawBitmap(this.bitmap, this.x, this.y, null);
	}
	
}
