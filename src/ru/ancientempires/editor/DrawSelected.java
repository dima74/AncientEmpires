package ru.ancientempires.editor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ru.ancientempires.draws.Draw;

public class DrawSelected extends Draw
{
	
	private DrawChoose	choose;
	private int			h		= 2;
	private int			y;
	public int			x		= 0;			// координата центра полоски
	private Paint		paint	= new Paint();
								
	public DrawSelected(DrawChoose choose)
	{
		paint.setColor(Color.RED);
		this.choose = choose;
		y = choose.hBeforeBitmap - h * 2;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		int targetX = (int) choose.getCurrentBitmapLeftX();
		x += (targetX - x) / 2;
		canvas.drawRect(x, y, x + DrawChoose.mA, y + h, paint);
	}
	
}
