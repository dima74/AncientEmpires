package ru.ancientempires.framework;

import java.util.HashMap;
import java.util.HashSet;

public class Debug
{
	
	public static boolean					writeFirstLaunch	= false;
																
	public static HashMap<Object, Integer>	indexes				= new HashMap();
	public static HashSet<Object>			running				= new HashSet();
																
	private static void checkFirstLaunch()
	{
		checkTime();
		if (!writeFirstLaunch)
		{
			writeFirstLaunch = true;
			MyLog.l("");
			MyLog.l("");
			MyLog.l("------------------------------------------------");
			lastTime = System.currentTimeMillis();
		}
	}
	
	public static void create(Object activity)
	{
		checkFirstLaunch();
		checkCreate(activity);
	}
	
	private static void checkCreate(Object activity)
	{
		if (!indexes.containsKey(activity))
			indexes.put(activity, indexes.size());
	}
	
	public static void onStart(Object activity)
	{
		checkFirstLaunch();
		checkCreate(activity);
		MyLog.l("start " + getName(activity));
		running.add(activity);
	}
	
	public static void onStop(Object activity)
	{
		checkCreate(activity);
		MyLog.l("stop  " + getName(activity));
		running.remove(activity);
	}
	
	private static long lastTime = 0;
	
	private static void checkTime()
	{
		if (System.currentTimeMillis() - lastTime > 1000)
			MyLog.l("");
		lastTime = System.currentTimeMillis();
	}
	
	private static String getName(Object activity)
	{
		return String.format("%2d %s", indexes.get(activity), activity.getClass().getSimpleName().replace("Activity", ""));
	}
	
}
