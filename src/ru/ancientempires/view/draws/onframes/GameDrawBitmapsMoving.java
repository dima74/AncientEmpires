package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameDrawBitmapsMoving extends GameDrawBitmaps
{
	
	public int	startY;
	public int	startX;
	public int	endY;
	public int	endX;
	
	public GameDrawBitmapsMoving(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	@Override
	public GameDrawBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		return super.setBitmaps(bitmaps);
	}
	
	public GameDrawBitmapsMoving animate(int frameToStart, int startY, int startX, int endY, int endX, int frameLength)
	{
		this.startY = startY;
		this.startX = startX;
		this.endY = endY;
		this.endX = endX;
		super.animate(frameToStart, frameLength);
		return this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int frameLeft = this.frameEnd - this.gameDraw.iFrame;
		this.y = (frameLeft * this.startY + framePass * this.endY) / this.frameLength;
		this.x = (frameLeft * this.startX + framePass * this.endX) / this.frameLength;
		super.draw(canvas);
	}
	
}
