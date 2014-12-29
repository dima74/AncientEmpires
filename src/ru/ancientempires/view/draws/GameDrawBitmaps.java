package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmaps extends GameDraw
{
	
	private static final int	FRAMES_ANIMATE	= GameDrawDecreaseHealth.FRAMES_ANIMATE / 2;
	
	private Bitmap[]			bitmaps;
	private int					frameStart;
	
	private int					y;
	private int					x;
	
	public GameDrawBitmaps(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public void initAnimate(int y, int x)
	{
		this.y = y;
		this.x = x;
	}
	
	public void startAnimate()
	{
		this.frameStart = this.gameDraw.iFrame;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int framePass = this.gameDraw.iFrame - this.frameStart;
		if (framePass < 0 || framePass >= GameDrawBitmaps.FRAMES_ANIMATE)
			return;
		
		int i = framePass * 2 * this.bitmaps.length / GameDrawBitmaps.FRAMES_ANIMATE % this.bitmaps.length;
		canvas.drawBitmap(this.bitmaps[i], this.x, this.y, null);
	}
	
}
