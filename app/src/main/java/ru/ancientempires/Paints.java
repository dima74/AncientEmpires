package ru.ancientempires;

import android.graphics.Color;
import android.graphics.Paint;

public class Paints
{
	
	public static final Paint   ANTI_ALIAS_FLAG = new Paint(Paint.ANTI_ALIAS_FLAG);
	public static final Paint   WHITE           = new Paint();
	public static final Paint   RED             = new Paint();
	public static final Paint[] MY_COLORS       = new Paint[MyColor.values().length];

	static
	{
		WHITE.setColor(Color.WHITE);
		RED.setColor(Color.RED);
		for (MyColor color : MyColor.values())
		{
			Paint paint = new Paint();
			paint.setColor(color.showColor);
			MY_COLORS[color.ordinal()] = paint;
		}
	}
	
}
