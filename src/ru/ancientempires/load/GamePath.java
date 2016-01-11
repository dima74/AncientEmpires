package ru.ancientempires.load;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.helpers.JsonHelper;

// Этот класс описывает описывает общую информацию сохранения (нм меняется от снимка к снимку)
public class GamePath
{
	
	/*
		isBaseGame canChooseTeams
		0 0 каждое сохранение
		0 1 невозможно
		1 0 базовая игра - кампания
		1 1 базовая игра - мультиплеер
	*/
	
	public String	path;
	public String	gameID;
	public String	baseGameID;
	public String	nextGameID;
	public int		numberPlayers;
	public boolean	isBaseGame;
	public boolean	canChooseTeams;
	public int		h;
	public int		w;
	
	public GamePath()
	{}
	
	// Создаёт новый объект GamePath по заданному info.json
	public GamePath(Client client, String path) throws IOException
	{
		load(path);
		client.allGames.put(gameID, this);
	}
	
	// Создаёт новый объект GamePath представляющий собой копию info.json с новым gameID
	public GamePath(Client client, String path, String gameID) throws IOException
	{
		load(path);
		baseGameID = baseGameID != null ? baseGameID : this.gameID;
		this.gameID = gameID;
		client.allGames.put(gameID, this);
	}
	
	private void load(String path) throws IOException
	{
		this.path = path;
		JsonReader reader = Client.client.gamesLoader.getReader(path + "info.json");
		reader.beginObject();
		gameID = JsonHelper.readString(reader, "gameID");
		baseGameID = JsonHelper.readString(reader, "baseGameID");
		if (baseGameID.isEmpty())
			baseGameID = null;
		nextGameID = JsonHelper.readString(reader, "nextGameID");
		if (nextGameID.isEmpty())
			nextGameID = null;
		numberPlayers = JsonHelper.readInt(reader, "numberPlayers");
		isBaseGame = JsonHelper.readBoolean(reader, "isBaseGame");
		canChooseTeams = JsonHelper.readBoolean(reader, "canChooseTeams");
		h = JsonHelper.readInt(reader, "h");
		w = JsonHelper.readInt(reader, "w");
		reader.endObject();
	}
	
	public FileLoader getLoader()
	{
		return Client.client.gamesLoader.getLoader(path);
	}
	
	public void save() throws IOException
	{
		FileLoader loader = getLoader();
		JsonWriter writer = loader.getWriter("info.json");
		writer.beginObject();
		writer.name("gameID").value(gameID);
		writer.name("baseGameID").value(baseGameID);
		writer.name("nextGameID").value(nextGameID);
		writer.name("numberPlayers").value(numberPlayers);
		writer.name("isBaseGame").value(isBaseGame);
		writer.name("canChooseTeams").value(canChooseTeams);
		writer.name("h").value(h);
		writer.name("w").value(w);
		writer.endObject();
		writer.close();
	}
	
}
