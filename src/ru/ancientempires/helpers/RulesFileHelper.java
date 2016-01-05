package ru.ancientempires.helpers;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import ru.ancientempires.client.Client;

public class RulesFileHelper
{
	
	public static JsonReader getReader(String name) throws IOException
	{
		return Client.client.fileLoader.getReader("rules/" + name);
	}
	
}
