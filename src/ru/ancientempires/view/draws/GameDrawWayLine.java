package ru.ancientempires.view.draws;

import ru.ancientempires.helpers.Point;
import android.graphics.Canvas;

public class GameDrawWayLine extends GameDraw
{
	
	private float[]	wayPoints;
	
	public GameDrawWayLine(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void update(Point[] ways)
	{
		this.wayPoints = new float[(ways.length - 1) * 4];
		
		// extra 1/12 pixels
		float one12 = 1.0f / 12;
		for (int k = 0; k < ways.length - 1; k++)
		{
			float y1 = ways[k].i;// wayYs[k];
			float x1 = ways[k].j;// wayXs[k];
			float y2 = ways[k + 1].i;// wayYs[k + 1];
			float x2 = ways[k + 1].j;// wayXs[k + 1];
			if (y1 == y2)
				if (x1 < x2)
				{
					x1 -= one12;
					x2 += one12;
				}
				else
				{
					x2 -= one12;
					x1 += one12;
				}
			else if (x1 == x2)
				if (y1 < y2)
				{
					y1 -= one12;
					y2 += one12;
				}
				else
				{
					y2 -= one12;
					y1 += one12;
				}
			this.wayPoints[k * 4 + 0] = (x1 + 0.5f) * GameDraw.A;
			this.wayPoints[k * 4 + 1] = (y1 + 0.5f) * GameDraw.A;
			this.wayPoints[k * 4 + 2] = (x2 + 0.5f) * GameDraw.A;
			this.wayPoints[k * 4 + 3] = (y2 + 0.5f) * GameDraw.A;
		}
	}
	
	public void destroy()
	{
		this.wayPoints = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.wayPoints != null)
			canvas.drawLines(this.wayPoints, Paints.LINE_PAINT);
	}
	
}
