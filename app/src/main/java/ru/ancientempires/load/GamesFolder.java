package ru.ancientempires.load;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.framework.MyLog;

public class GamesFolder
{
	
	public ArrayList<GamePath> games;
	public String              folderID;
	public String              name;
	public String              path;

	public int     numberGames;
	public boolean isCampaign;
	public boolean isSave;

	public String getName(int i, GamePath game)
	{
		String name = game.name;
		if (isCampaign)
			name = i + 1 + ". " + name;
		return name;
	}
	
	// для AllGamesConverter
	public GamesFolder(String folderID, int numberGames)
	{
		this.folderID = folderID;
		path = folderID.replace('.', '/') + "/";
		this.numberGames = numberGames;
	}
	
	public GamesFolder(String folderID) throws Exception
	{
		this.folderID = folderID;
		path = folderID.replace('.', '/') + "/";
		name = Client.client.localization.loadName(Client.client.gamesLoader.getLoader(path));
		
		load();
		if ("save".equals(folderID) && false)
		{
			numberGames = 0;
			save();
		}
		games = new ArrayList<>();
		for (int i = 0; i < numberGames; i++)
			games.add(GamePath.get(path + i + "/", true));
	}
	
	public void load() throws IOException
	{
		JsonReader reader = Client.client.gamesLoader.getReader(path + "info.json");
		JsonObject object = new JsonParser().parse(reader).getAsJsonObject();
		reader.close();
		
		numberGames = object.get("numberGames").getAsInt();
		isCampaign = object.get("isCampaign").getAsBoolean();
		isSave = object.get("isSave").getAsBoolean();
	}
	
	public void save() throws IOException
	{
		JsonObject object = new JsonObject();
		object.addProperty("numberGames", numberGames);
		object.addProperty("isCampaign", isCampaign);
		object.addProperty("isSave", isSave);
		
		JsonWriter writer = Client.client.gamesLoader.getWriter(path + "info.json");
		new Gson().toJson(object, writer);
		writer.close();
	}
	
	public void add(GamePath game) throws IOException
	{
		games.add(game);
		numberGames++;
		save();
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	public void deleteAll() throws IOException
	{
		for (GamePath game : games)
			deleteFolder(game.getLoader().getFile(""));
		numberGames = 0;
		save();
	}
	
	public void deleteFolder(File folder)
	{
		MyLog.l(folder);
		if (!folder.exists())
			return;
		MyAssert.a(folder.isDirectory());
		for (File file : folder.listFiles())
			if (file.isDirectory())
				deleteFolder(file);
			else
				file.delete();
		folder.delete();
	}
	
}
