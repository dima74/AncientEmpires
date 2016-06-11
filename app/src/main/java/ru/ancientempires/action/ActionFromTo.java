package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.serializable.LoaderInfo;

public abstract class ActionFromTo extends ActionTo
{
	
	public int i;
	public int j;
	
	public ActionFromTo setIJ(int i, int j)
	{
		this.i = i;
		this.j = j;
		return this;
	}
	
	@Override
	public boolean check()
	{
		return game.checkCoordinates(i, j) && game.checkCoordinates(targetI, targetJ);
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		super.load(input);
		i = input.readShort();
		j = input.readShort();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		super.save(output);
		output.writeShort(i);
		output.writeShort(j);
	}
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeInt(i);
		output.writeInt(j);
	}

	public ActionFromTo fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		i = input.readInt();
		j = input.readInt();
		return this;
	}

}
