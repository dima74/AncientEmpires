package ru.ancientempires.framework;

public class LogHelper
{
	
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
