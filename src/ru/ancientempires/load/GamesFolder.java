package ru.ancientempires.load;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.Localization;
import ru.ancientempires.client.Client;

public class GamesFolder
{
	
	public GamePath[]		games;
	public GamesFolder[]	folders;
	public GamesFolder		parentFolder;
	public String			folderID;
	public String			name;
	
	public GamesFolder(String path, GamesFolder parentFolder) throws Exception
	{
		if (path != "")
		{
			String folderID = path.replace('/', '.').substring(0, path.length() - 1);
			Client.client.allFolders.put(folderID, this);
			name = Localization.get(folderID);
		}
		this.parentFolder = parentFolder;
		JsonReader reader = Client.client.gamesLoader.getReader(path + "info.json");
		reader.beginObject();
		while (reader.peek() == JsonToken.NAME)
		{
			String name = reader.nextName();
			if ("folders".equals(name))
			{
				String[] strings = new Gson().fromJson(reader, String[].class);
				folders = new GamesFolder[strings.length];
				for (int i = 0; i < strings.length; i++)
					folders[i] = new GamesFolder(path + strings[i], this);
			}
			else if ("games".equals(name))
			{
				String[] strings = new Gson().fromJson(reader, String[].class);
				games = new GamePath[strings.length];
				for (int i = 0; i < strings.length; i++)
					games[i] = GamePath.get(path + strings[i], true);
			}
		}
		reader.endObject();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
