package ru.ancientempires.framework;

import android.util.Log;

public class ALog extends MyLog
{
	
	@Override
	protected void write(String string)
	{
		Log.e("ae", string);
	}
	
}
