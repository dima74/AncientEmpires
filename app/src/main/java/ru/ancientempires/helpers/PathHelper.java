package ru.ancientempires.helpers;

public class PathHelper
{
	
	public static String get(String path)
	{
		String[] a = path.split("/");
		int j = 0;
		for (int i = 0; i < a.length; i++)
			if (a[i].equals(".."))
				j--;
			else
			{
				a[j] = a[i];
				j++;
			}
		String ret = a[0];
		for (int i = 1; i < j; i++)
			ret += "/" + a[i];
		if (path.endsWith("/"))
			ret += "/";
		return ret;
	}
}
