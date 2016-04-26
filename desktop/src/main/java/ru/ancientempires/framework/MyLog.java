package ru.ancientempires.framework;

public class MyLog
{
	
	public static void l(Object... args)
	{
		System.out.println(LogHelper.getString(args));
	}
	
}
