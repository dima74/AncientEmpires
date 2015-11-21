package ru.ancientempires.view.draws;

import android.graphics.Canvas;
import android.graphics.Paint;
import ru.ancientempires.Point;

public class GameDrawWayLine extends GameDraw
{
	
	public static Paint LINE_PAINT = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	static
	{
		GameDrawWayLine.LINE_PAINT.setStrokeWidth(GameDraw.A / 3);
		GameDrawWayLine.LINE_PAINT.setColor(0xFFE10052);
	}
	
	private float[] wayPoints;
	
	public void update(Point[] points)
	{
		wayPoints = new float[(points.length - 1) * 4];
		
		// extra 1/12 pixels
		float one12 = 1.0f / 12;
		for (int k = 0; k < points.length - 1; k++)
		{
			float y1 = points[k].i;// wayYs[k];
			float x1 = points[k].j;// wayXs[k];
			float y2 = points[k + 1].i;// wayYs[k + 1];
			float x2 = points[k + 1].j;// wayXs[k + 1];
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
			wayPoints[k * 4 + 0] = (x1 + 0.5f) * GameDraw.A;
			wayPoints[k * 4 + 1] = (y1 + 0.5f) * GameDraw.A;
			wayPoints[k * 4 + 2] = (x2 + 0.5f) * GameDraw.A;
			wayPoints[k * 4 + 3] = (y2 + 0.5f) * GameDraw.A;
		}
	}
	
	public void destroy()
	{
		wayPoints = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (wayPoints != null)
			canvas.drawLines(wayPoints, GameDrawWayLine.LINE_PAINT);
	}
	
}
