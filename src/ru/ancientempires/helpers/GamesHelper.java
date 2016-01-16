package ru.ancientempires.helpers;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import ru.ancientempires.client.Client;

public class GamesHelper
{
	
	public static JsonReader getReader(String name) throws IOException
	{
		return Client.client.gamesLoader.getReader(name);
	}
	
	public static FileLoader getLoader(String path)
	{
		return Client.client.gamesLoader.getLoader(path);
	}
	
}
