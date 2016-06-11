package ru.ancientempires;

import java.util.Locale;

public enum MyColor
{
	GREY(0xFF121212),
	RED(0xFFFF0000),
	GREEN(0xFF00FF00),
	BLUE(0xFF0000FF),
	BLACK(0xFF007696);
	
	public static MyColor[] playersColors()
	{
		return new MyColor[]
				{
						BLUE,
						RED,
						GREEN,
						BLACK
				};
	}
	
	public int showColor;
	
	MyColor(int showColor)
	{
		this.showColor = showColor;
	}
	
	public String folderName()
	{
		return name().toLowerCase(Locale.ENGLISH);
	}
	
}
