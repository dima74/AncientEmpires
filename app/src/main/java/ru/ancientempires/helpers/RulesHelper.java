package ru.ancientempires.helpers;

import com.google.gson.stream.JsonReader;

import java.io.IOException;

import ru.ancientempires.client.Client;

public class RulesHelper
{
	
	public static JsonReader getReader(String name) throws IOException
	{
		return Client.client.rulesLoader.getReader(name);
	}
	
}
