package ru.ancientempires.framework;

import android.app.Activity;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import ru.ancientempires.client.AndroidClientHelper;
import ru.ancientempires.client.Client;

public class Debug
{
	
	public static boolean writeFirstLaunch = false;

	public static HashMap<Object, Integer> indexes = new HashMap();
	public static HashSet<Object> running = new HashSet();

	private static void checkFirstLaunch()
	{
		checkTime();
		if (!writeFirstLaunch)
		{
			writeFirstLaunch = true;
			MyLog.l("");
			MyLog.l("");
			MyLog.l("------------------------------------------------");
			MyLog.l(new Date());
			lastTime = System.currentTimeMillis();
		}
	}
	
	private static void checkClient(Object activity)
	{
		if (Client.client == null)
			try
			{
				MyAssert.a(activity instanceof Activity);
				Client.client = new Client(new AndroidClientHelper((Activity) activity));
				Client.client.startLoadParts12();
			} catch (IOException e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
	}
	
	public static void onCreate(Object activity)
	{
		checkFirstLaunch();
		checkCreate(activity);
		checkClient(activity);
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
		checkClient(activity);
		MyLog.l("start " + getName(activity));
		running.add(activity);
	}
	
	public static void onStop(Object activity)
	{
		checkCreate(activity);
		MyLog.l("stop  " + getName(activity));
		running.remove(activity);
		if (running.isEmpty())
			Client.client = null;
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
