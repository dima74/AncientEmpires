package ru.ancientempires.actions;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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
