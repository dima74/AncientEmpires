package ru.ancientempires.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

@IndexSubclasses
public abstract class Task extends AbstractGameHandler implements SerializableJson
{
	
	@Exclude public int turnToRun;
	
	public Task setTurn(int differenceTurn)
	{
		turnToRun = game.currentTurn + differenceTurn;
		return this;
	}
	
	public void register()
	{
		game.registerTask(this);
	}
	
	public abstract void run();
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = SerializableJsonHelper.toJson(this);
		return object;
	}

	public Task fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		return this;
	}

	static public Task[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Task[] array = new Task[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), Task.class);
		return array;
	}

}
