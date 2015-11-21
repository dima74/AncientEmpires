package ru.ancientempires.view.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmaps extends GameDrawOnFramesWithRangeValues
{
	
	public static final int	FRAMES_FOR_BITMAP	= 2;
	public int				framesForBitmap		= GameDrawBitmaps.FRAMES_FOR_BITMAP;
	
	public Bitmap[] bitmaps;
	
	public int	y;
	public int	x;
	
	public GameDrawBitmaps setFramesForBitmap(int framesForBitmap)
	{
		this.framesForBitmap = framesForBitmap;
		return this;
	}
	
	public GameDrawBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public GameDrawBitmaps setYX(int y, int x)
	{
		this.y = y;
		this.x = x;
		return this;
	}
	
	public GameDrawBitmaps animateRepeat(int repeat)
	{
		animateRange(0, bitmaps.length * framesForBitmap * repeat - 1);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas, int value)
	{
		canvas.drawBitmap(bitmaps[value / framesForBitmap % bitmaps.length], x, y, null);
	}
	
}
