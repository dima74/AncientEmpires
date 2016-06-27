package ru.ancientempires.tasks;

import com.google.gson.JsonObject;

import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.LoaderInfo;

public class TaskRemoveTombstone extends TaskFrom
{
	
	public TaskRemoveTombstone()
	{}
	
	public TaskRemoveTombstone(Game game)
	{
		setGame(game);
	}
	
	@Override
	public void run()
	{
		game.fieldUnitsDead[i][j] = null;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		return object;
	}

	public TaskRemoveTombstone fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		return this;
	}

}
