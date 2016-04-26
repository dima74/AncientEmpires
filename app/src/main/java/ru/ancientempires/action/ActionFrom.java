package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ActionFrom extends Action
{
	
	public int	i;
	public int	j;
	
	public ActionFrom setIJ(int i, int j)
	{
		this.i = i;
		this.j = j;
		return this;
	}
	
	@Override
	public boolean check()
	{
		return game.checkCoordinates(i, j);
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		i = input.readShort();
		j = input.readShort();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeShort(i);
		output.writeShort(j);
	}
	
}
