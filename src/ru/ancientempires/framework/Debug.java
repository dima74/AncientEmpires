package ru.ancientempires.framework;

import java.util.HashMap;
import java.util.HashSet;

public class Debug
{
	
	public static boolean writeFirstLaunch = false;
	
	public static HashMap<Class<?>, Integer>	counts			= new HashMap();
	public static HashMap<Object, Integer>		numbers			= new HashMap();
	public static HashMap<Object, String>		numberSpaces	= new HashMap();
	public static HashSet<Object>				afterOnStart	= new HashSet();
	
	public static void create(Object activity)
	{
		if (!Debug.writeFirstLaunch)
		{
			Debug.writeFirstLaunch = true;
			MyLog.l("");
			MyLog.l("");
			MyLog.l("------------------------------------------------");
			Debug.lastTime = System.currentTimeMillis();
		}
		
		if (!Debug.counts.containsKey(activity.getClass()))
			Debug.counts.put(activity.getClass(), 0);
		Debug.numbers.put(activity, Debug.counts.get(activity.getClass()));
		
		String spaces = "";
		for (int i = 0; i < Debug.afterOnStart.size(); i++)
			spaces += " ";
		Debug.numberSpaces.put(activity, spaces);
	}
	
	public static void onStart(Object activity)
	{
		Debug.checkTime();
		MyLog.l(Debug.getSpaces(activity) + "start " + Debug.getName(activity));
		Debug.afterOnStart.add(activity);
	}
	
	public static void onStop(Object activity)
	{
		MyLog.l(Debug.getSpaces(activity) + "stop " + Debug.getName(activity));
		Debug.afterOnStart.remove(activity);
	}
	
	private static long lastTime = 0;
	
	private static void checkTime()
	{
		if (System.currentTimeMillis() - Debug.lastTime > 500)
			MyLog.l("");
		Debug.lastTime = System.currentTimeMillis();
	}
	
	private static String getSpaces(Object activity)
	{
		return Debug.numberSpaces.get(activity);
	}
	
	private static String getName(Object activity)
	{
		return activity.getClass().getSimpleName().replace("Activity", "");
	}
	
}
