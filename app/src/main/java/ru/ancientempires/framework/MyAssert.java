package ru.ancientempires.framework;

import java.io.DataOutputStream;
import java.io.PrintWriter;

import ru.ancientempires.serializable.SerializableJson;

public class MyAssert
{
	
	public static DataOutputStream output;
	public static PrintWriter      outputText;

	public static void a(boolean booleanTrue)
	{
		a(booleanTrue, null);
	}

	public static void a(boolean booleanTrue, String message)
	{
		if (!booleanTrue)
		{
			MyLog.l("!!!");
			new Exception().printStackTrace();
			//System.exit(1);
			System.out.print("");
			throw new RuntimeException();
		}
	}

	private static void method()
	{}
	
	public static void a(Object one, Object two)
	{
		boolean equals = one.equals(two);
		String a = one instanceof SerializableJson ? ((SerializableJson) one).toJson().toString() : one.toString();
		String b = two instanceof SerializableJson ? ((SerializableJson) two).toJson().toString() : two.toString();
		MyAssert.a(equals, !equals ? diff(a, b) : null);
	}

	public static String diff(String a, String b)
	{
		int n = Math.min(a.length(), b.length());
		for (int i = 0; i <= n; i++)
			if (i == n || a.charAt(i) != b.charAt(i))
			{
				int length = 40;
				String partA = a.substring(i, i + length);
				String partB = b.substring(i, i + length);
				return String.format("1: %s\n2: %s\n\n1: %s\n2: %s", a.substring(0, length), b.substring(0, length), partA, partB);
			}
		return null;
	}
	
}
