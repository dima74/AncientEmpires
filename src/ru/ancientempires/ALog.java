package ru.ancientempires;

import android.util.Log;
import ru.ancientempires.framework.MyLog;

public class ALog extends MyLog
{
	
	@Override
	protected void write(String string)
	{
		Log.wtf("ae", string);
	}
	
}
