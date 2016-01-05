package ru.ancientempires.framework;

public class MyAssert
{
	
	public static void a(boolean boolTrue)
	{
		if (!boolTrue)
		{
			MyLog.l("exception");
			MyLog.l(new Exception().getStackTrace());
		}
	}
	
	public static void a(Object one, Object two)
	{
		MyAssert.a(one.equals(two));
	}
	
}
