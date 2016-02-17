package ru.ancientempires.load;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.Game;
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
	
	public GamePath()
	{}
	
	public GamePath(Game game, String gameID)
	{
		this.gameID = gameID;
		numberPlayers = game.players.length;
		path = gameID.replace('.', '/') + "/";
		h = game.h;
		w = game.w;
		defaultLocalization = "en_US";
		localizations = new String[]
		{
				"ru_RU"
		};
		game.path = this;
	}
	
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
		name = Client.client.localization.loadName(getLoader());
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
	
	public GamesFolder getFolder()
	{
		return Client.client.allFolders.get(gameID.subSequence(0, gameID.lastIndexOf('.')));
	}
	
}
