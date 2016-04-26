package ru.ancientempires.helpers;

public class BeatifullHelper
{
	
	public static String toBeatifullString(String s, int lentgh)
	{
		while (s.length() < lentgh)
			s = " " + s;
		return s;
	}
	
	public static String toBeatifullString(boolean b, int lentgh)
	{
		return b ? "1" : "0";
	}
	
}
