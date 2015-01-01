package ru.ancientempires.view.draws;

import android.graphics.Canvas;

public class GameDrawSinus extends GameDrawOnFrames
{
	
	public static final int[]	YS	= new int[]
									{
			9, 9, 9, 9, 6, 6, 6, 6, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4,
			4, 6, 6, 6, 6, 9, 9, 9, 9, 13, 13, 13, 13, 11, 11, 11, 11, 10, 10, 10,
			10, 10, 10, 10, 10, 11, 11, 11, 11, 13, 13, 13, 13, 12, 12, 12, 12, 12,
			12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13,
			13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13
									};
	
	private int					y	= 0;
	private int					x	= 0;
	
	public GameDrawSinus(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public GameDrawSinus setCoord(int y, int x)
	{
		this.y = y;
		this.x = x;
		return this;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int i = framePass * GameDrawSinus.YS.length / this.frameLength;
		
		int offsetY = (int) (this.y + GameDrawSinus.YS[i] * GameDraw.a);
		canvas.translate(this.x, offsetY);
		drawChild(canvas, i);
		canvas.translate(-this.x, -offsetY);
	}
	
	public void drawChild(Canvas canvas, int i)
	{}
	
}
