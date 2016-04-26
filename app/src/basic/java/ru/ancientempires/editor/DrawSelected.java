package ru.ancientempires.editor;

import android.graphics.Canvas;
import ru.ancientempires.Paints;
import ru.ancientempires.draws.Draw;

public class DrawSelected extends Draw
{
	
	public static int	h	= 2;
	public int			y;
	public int			x;		// координата центра полоски
	public int			targetX;
						
	public DrawSelected(int y)
	{
		this.y = y;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		x += (targetX - x) / 2;
		canvas.drawRect(x, y, x + DrawChoose.mA, y + h, Paints.RED);
	}
	
}
