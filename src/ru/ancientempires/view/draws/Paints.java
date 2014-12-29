package ru.ancientempires.view.draws;

import android.graphics.Paint;

public class Paints
{
	
	public static Paint	LINE_PAINT	= new Paint(Paint.ANTI_ALIAS_FLAG);
	public static Paint	_PAINT		= new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public static void init()
	{
		Paints.LINE_PAINT.setStrokeWidth(GameDraw.A / 3);
		Paints.LINE_PAINT.setColor(0xFFE10052);
	}
	
}
