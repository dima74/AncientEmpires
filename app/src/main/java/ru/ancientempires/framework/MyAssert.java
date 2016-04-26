package ru.ancientempires.framework;

import java.io.DataOutputStream;
import java.io.PrintWriter;

public class MyAssert
{
	
	public static DataOutputStream output;
	public static PrintWriter outputText;

	public static void a(boolean booleanTrue)
	{
		if (!booleanTrue)
		{
			MyLog.l("!!!");
			MyLog.l(new Exception().getStackTrace());
		}
	}
	
	private static void method()
	{
	}
	
	public static void a(Object one, Object two)
	{
		MyAssert.a(one.equals(two));
	}
	
}
