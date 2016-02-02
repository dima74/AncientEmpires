package ru.ancientempires.load;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;

public class GamesFolder
{
	
	public GamePath[]	games;
	public String		folderID;
	public String		name;
	
	public GamesFolder(String folderID) throws Exception
	{
		this.folderID = folderID;
		name = Localization.get(folderID);
		String path = folderID + "/";
		
		JsonReader reader = Client.client.gamesLoader.getReader(path + "info.json");
		int numberGames = new Gson().fromJson(reader, Integer.class);
		reader.close();
		
		games = new GamePath[numberGames];
		for (int i = 0; i < numberGames; i++)
			games[i] = GamePath.get(path + i + "/", true);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
