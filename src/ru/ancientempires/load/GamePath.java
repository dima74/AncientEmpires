package ru.ancientempires.load;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.save.GameSaveLoader;

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
	
	transient public String	path;
	public String			gameID;
	public String			baseGameID;
	public String			nextGameID;
	public int				numberPlayers;
	public boolean			isBaseGame;
	public boolean			canChooseTeams;
	public int				h;
	public int				w;
	
	public Rules getRules()
	{
		return Client.client.rules;
	}
	
	public String			defaultLocalization;
	public String[]			localizations;
	transient public String	name;
	
	public FileLoader getLoader()
	{
		return Client.client.gamesLoader.getLoader(path);
	}
	
	public GameSaveLoader getGameSaveLoader() throws IOException
	{
		return new GameSaveLoader(getLoader()).load();
	}
	
	// Создаёт новый объект GamePath представляющий собой копию info.json с новым gameID
	public static GamePath get(String path, boolean addToAllGames) throws Exception
	{
		GamePath gamePath = new Gson().fromJson(Client.client.gamesLoader.getReader(path + "info.json"), GamePath.class);
		gamePath.path = path;
		gamePath.loadName();
		if (addToAllGames)
			Client.client.allGames.put(gamePath.gameID, gamePath);
		return gamePath;
	}
	
	private void loadName() throws IOException
	{
		JsonReader reader = Client.client.localization.getReader(getLoader());
		reader.beginObject();
		MyAssert.a("name".equals(reader.nextName()));
		name = reader.nextString();
		reader.close();
	}
	
	public GamePath copyTo(String newPath, String newID) throws IOException
	{
		baseGameID = baseGameID != null ? baseGameID : gameID;
		path = newPath;
		gameID = newID;
		save();
		Client.client.allGames.put(gameID, this);
		return this;
	}
	
	public void save() throws IOException
	{
		JsonWriter writer = getLoader().getWriter("info.json");
		new Gson().toJson(this, GamePath.class, writer);
		writer.close();
	}
	
}
