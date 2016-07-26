package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.draws.BaseDrawMain;

public class DrawBitmapsMoving extends DrawBitmaps
{
	
	public int startY;
	public int startX;
	public int endY;
	public int endX;

	public DrawBitmapsMoving(BaseDrawMain mainBase)
	{
		super(mainBase);
	}
	
	public DrawBitmapsMoving setLineYX(int startY, int startX, int endY, int endX)
	{
		this.startY = startY;
		this.startX = startX;
		this.endY = endY;
		this.endX = endX;
		return this;
	}

	public DrawBitmapsMoving animateDelta(int delta)
	{
		int deltaY = Math.abs(endY - startY);
		int deltaX = Math.abs(endX - startX);
		animateRange(0, Math.max(deltaY, deltaX) / delta);
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
