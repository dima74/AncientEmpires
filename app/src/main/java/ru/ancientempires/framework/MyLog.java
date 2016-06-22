package ru.ancientempires.framework;

public class MyLog
{

	public static void l(Object... args)
	{
		LogWriter.write(getString(args));
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

	public static void l(byte[] b, int n)
	{
		String s = "length: " + n + ", data:";
		for (int i = 0; i < n; i += 8)
		{
			s += " ";
			for (int j = 0; j < 8 && i + j < n; j++)
				s += String.format("%02X", b[i + j]);
		}
		LogWriter.write(s);
	}

	public static void f(String format, Object... args)
	{
		l(String.format(format, args));
	}

}
