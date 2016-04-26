package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.action.result.ActionResultGetRandomNumber;
import ru.ancientempires.model.Game;

public class ActionGetRandomNumber extends Action
{
	
	public int							bound;
	public ActionResultGetRandomNumber	result	= new ActionResultGetRandomNumber();
	
	public ActionGetRandomNumber setBound(int bound)
	{
		this.bound = bound;
		return this;
	}
	
	@Override
	public ActionResultGetRandomNumber perform(Game game)
	{
		performBase(game);
		return result;
	}
	
	@Override
	public void performQuick()
	{
		result.number = game.random.nextInt(bound);
		// System.out.println(game + " " + result.number);
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeInt(bound);
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		bound = input.readInt();
	}
	
}
