package ru.ancientempires.helpers;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import ru.ancientempires.framework.MyAssert;

public class JsonHelper
{
	
	public static String readString(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		return reader.nextString();
	}
	
	public static int readInt(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		return reader.nextInt();
	}
	
	public static boolean readBoolean(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		return reader.nextBoolean();
	}
	
	public static long readLong(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		return reader.nextLong();
	}
	
}
