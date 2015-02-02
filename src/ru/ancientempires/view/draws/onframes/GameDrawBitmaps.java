package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmaps extends GameDrawOnFrames
{
	
	public static final int	FRAMES_ANIMATE_LONG		= GameDrawDecreaseHealth.FRAMES_ANIMATE / 2;	// 24
	public static final int	FRAMES_ANIMATE_SHORT	= GameDrawBitmaps.FRAMES_ANIMATE_LONG / 2;		// 12
																									
	private Bitmap[]		bitmaps;
	
	public int				y;
	public int				x;
	
	public GameDrawBitmaps(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public GameDrawBitmaps animate(int frameToStart, int y, int x, int frameLength)
	{
		this.y = y;
		this.x = x;
		animate(frameToStart, frameLength);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int i = framePass * 2 * this.bitmaps.length / GameDrawBitmaps.FRAMES_ANIMATE_LONG % this.bitmaps.length;
		canvas.drawBitmap(this.bitmaps[i], this.x, this.y, null);
	}
	
}
