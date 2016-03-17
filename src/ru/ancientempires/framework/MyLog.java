package ru.ancientempires.framework;

import android.util.Log;

public abstract class MyLog
{
	
	public static void l(Object... args)
	{
		MyAssert.a(args != null);
		Log.wtf("ru.ae", LogHelper.getString(args));
	}
	
}
