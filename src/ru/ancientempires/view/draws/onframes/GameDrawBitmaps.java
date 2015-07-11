package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmaps extends GameDrawOnFrames
{
	
	public static final int	FRAMES_FOR_BITMAP	= 2;
	public int				framesForBitmap		= GameDrawBitmaps.FRAMES_FOR_BITMAP;
	
	public Bitmap[]			bitmaps;
	
	public int				y;
	public int				x;
	
	public GameDrawBitmaps(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
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
	
	public GameDrawBitmaps animateRepeat(int frameToStart, int repeat)
	{
		animate(frameToStart, this.bitmaps.length * this.framesForBitmap * repeat);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		int framePass = this.gameDraw.iFrame - this.frameStart;
		// int i = framePass * this.bitmaps.length / this.frameLength;
		int i = framePass / this.framesForBitmap % this.bitmaps.length;
		canvas.drawBitmap(this.bitmaps[i], this.x, this.y, null);
	}
	
}
