package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.view.draws.GameDrawMain;

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
	
	public GameDrawBitmapsMoving setLineYX(int startY, int startX, int endY, int endX)
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
		this.y = (this.frameLeft * this.startY + this.framePass * this.endY) / (this.frameCount - 1);
		this.x = (this.frameLeft * this.startX + this.framePass * this.endX) / (this.frameCount - 1);
		super.draw(canvas, value);
	}
	
}
