package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.client.Client;

public abstract class Task
{
	
	public static List<Class<? extends Task>>	tasks	= Arrays.asList(
			TaskRemoveTombstone.class,
			TaskIncreaseUnitAttack.class,
			TaskIncreaseUnitDefence.class);
	public int									turnToRun;
	
	public Task setTurn(int differenceTurn)
	{
		turnToRun = GameHandler.game.currentTurn + differenceTurn;
		return this;
	}
	
	public void register()
	{
		Client.getGame().registerTask(this);
	}
	
	public abstract void run();
	
	public int ordinal()
	{
		return Task.tasks.indexOf(getClass());
	}
	
	public void saveBase(DataOutputStream output) throws IOException
	{
		output.writeByte(ordinal());
		save(output);
		output.close();
	}
	
	public void load(DataInputStream input) throws IOException
	{}
	
	public void save(DataOutputStream output) throws IOException
	{}
	
}
