package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class ActionTo extends Action
{
	
	public int	targetI;
	public int	targetJ;
	
	public ActionTo setTargetIJ(int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		return this;
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		targetI = input.readShort();
		targetJ = input.readShort();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeShort(targetI);
		output.writeShort(targetJ);
	}
	
}
