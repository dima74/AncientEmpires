package ru.ancientempires.framework;

import ru.ancientempires.client.Client;

public abstract class MyLog
{
	
	public static void l(Object... args)
	{
		MyAssert.a(args != null);
		MyAssert.a(Client.client != null);
		MyAssert.a(Client.client.log != null);
		Client.client.log.write(MyLog.getString(args));
	}
	
	public static String getString(Object... args)
	{
		final StringBuilder stringBuilder = new StringBuilder("");
		for (final Object object : args)
		{
			stringBuilder.append(object);
			stringBuilder.append(" ");
		}
		return stringBuilder.toString();
	}
	
}
