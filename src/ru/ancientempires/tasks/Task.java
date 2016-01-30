package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.handler.IGameHandler;
import ru.ancientempires.model.Game;

public abstract class Task extends IGameHandler
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
	
	public int turnToRun;
	
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
	
	public void load(DataInputStream input) throws IOException
	{}
	
	public void save(DataOutputStream output) throws IOException
	{}
	
}
