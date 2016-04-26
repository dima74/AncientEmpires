package ru.ancientempires.helpers;

import java.util.Locale;

public class ColorHelper
{
	
	/*
	public static int	R	= 0xFFFF0000;	// red
	public static int	G	= 0xFF00FF00;	// green
	public static int	B	= 0xFF0000FF;	// blue
	public static int	L	= 0xFF00016f;	// black
	
	// public static int L = 0xFF444444; // black
	// */
	
	public static int getColor(String colorString)
	{
		if (colorString.startsWith("0x"))
			colorString = colorString.substring(2);
		colorString = colorString.toUpperCase(Locale.ENGLISH);
		
		int color = 0;
		int p = 1;
		for (int i = colorString.length() - 1; i >= 0; i--)
		{
			char hexChar = colorString.charAt(i);
			int hex = ColorHelper.getNumberIntFromHex(hexChar);
			color += hex * p;
			p = p << 4;
		}
		
		return color;
	}
	
	public static int getNumberIntFromHex(char hex)
	{
		return hex > '9' ?
				(byte) hex - (byte) 'A' + 10 :
				(byte) hex - (byte) '0';
	}
	
	// 0x33008040 в png превратится в 0x33102000 (33/FF = 1/4, (1/4)*80 = 20, ARGB -> ABGR) в bitmap после BitmapFactory.decode...
	public static int toNormalColor(int color)
	{
		int a = ImageHelper.getA(color);
		int r = ImageHelper.getR(color);
		int g = ImageHelper.getG(color);
		int b = ImageHelper.getB(color);
		
		float k = a / (float) 0xFF;
		
		if (k != 0)
		{
			r = (int) (r / k);
			g = (int) (g / k);
			b = (int) (b / k);
		}
		
		color = ImageHelper.getColor(a, b, g, r);
		
		return color;
	}
	
	public static int toBitmapColor(int color)
	{
		int a = ImageHelper.getA(color);
		int r = ImageHelper.getR(color);
		int g = ImageHelper.getG(color);
		int b = ImageHelper.getB(color);
		
		float k = a / (float) 0xFF;
		
		r = (int) (r * k);
		g = (int) (g * k);
		b = (int) (b * k);
		
		color = ImageHelper.getColor(a, b, g, r);
		
		return color;
	}
	
}
