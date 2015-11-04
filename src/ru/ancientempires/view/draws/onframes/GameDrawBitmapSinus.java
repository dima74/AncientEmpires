package ru.ancientempires.view.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;

public class GameDrawBitmapSinus extends GameDrawOnFramesWithRangeValues
{
	
	// length = 48
	public static final int[] YS = new int[]
	{
			9, 9, 6, 6, 4, 4, 3, 3, 3, 3, 4, 4, 6, 6, 9, 9, 13, 13, 11, 11, 10, 10, 10, 10, 11, 11, 13, 13, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
	};
	
	private int		y	= 0;
	private int		x	= 0;
	private Bitmap	bitmap;
	
	public GameDrawBitmapSinus(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawBitmapSinus animate(int y, int x, Bitmap bitmap, int valuesForFrame)
	{
		this.y = y;
		this.x = x;
		this.bitmap = bitmap;
		animateRange(0, GameDrawBitmapSinus.YS.length - 1, valuesForFrame);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas, int value)
	{
		canvas.drawBitmap(this.bitmap, this.x, (int) (this.y + GameDrawBitmapSinus.YS[value] * GameDraw.a), null);
	}
	
}
