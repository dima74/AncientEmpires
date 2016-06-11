package ru.ancientempires.tasks;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.model.Game;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

@IndexSubclasses
public abstract class Task extends IGameHandler implements SerializableJson
{
	
	public static List<Class<? extends Task>> classes = Arrays.asList(
			TaskRemoveTombstone.class,
			TaskRemoveBonus.class);

	public static Task loadNew(DataInputStream input, Game game) throws Exception
	{
		int ordinal = input.readShort();
		Task task = Task.classes.get(ordinal).newInstance();
		task.setGame(game);
		task.load(input);
		return task;
	}
	
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
	
	public int ordinal()
	{
		return Task.classes.indexOf(getClass());
	}
	
	public void saveBase(DataOutputStream output) throws IOException
	{
		output.writeShort(ordinal());
		save(output);
	}
	
	public abstract void load(DataInputStream input) throws IOException;
	
	public abstract void save(DataOutputStream output) throws IOException;
	
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
