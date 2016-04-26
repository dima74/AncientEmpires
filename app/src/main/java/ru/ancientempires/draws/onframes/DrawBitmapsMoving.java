package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

public class DrawBitmapsMoving extends DrawBitmaps
{
	
	public int	startY;
	public int	startX;
	public int	endY;
	public int	endX;
	
	public DrawBitmapsMoving setLineYX(int startY, int startX, int endY, int endX)
	{
		this.startY = startY;
		this.startX = startX;
		this.endY = endY;
		this.endX = endX;
		return this;
	}
	
	@Override
	public void draw(Canvas canvas, int value)
	{
		y = (frameLeft * startY + framePass * endY) / (frameCount - 1);
		x = (frameLeft * startX + framePass * endX) / (frameCount - 1);
		super.draw(canvas, value);
	}
	
}
