package ru.ancientempires.helpers;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import ru.ancientempires.client.Client;

public class RulesHelper
{
	
	public static JsonReader getReader(String name) throws IOException
	{
		return Client.client.rulesLoader.getReader(name);
	}
	
}
