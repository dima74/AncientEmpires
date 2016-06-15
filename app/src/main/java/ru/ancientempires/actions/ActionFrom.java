package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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
