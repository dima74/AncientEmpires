package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.serializable.LoaderInfo;

public abstract class ActionFrom extends Action
{
	
	public int i;
	public int j;
	
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
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeInt(i);
		output.writeInt(j);
	}

	public ActionFrom fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		i = input.readInt();
		j = input.readInt();
		return this;
	}

}
