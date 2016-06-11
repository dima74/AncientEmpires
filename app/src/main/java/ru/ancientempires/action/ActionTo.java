package ru.ancientempires.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.serializable.LoaderInfo;

public abstract class ActionTo extends Action
{
	
	public int targetI;
	public int targetJ;
	
	public ActionTo setTargetIJ(int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		return this;
	}
	
	@Override
	public boolean check()
	{
		return game.checkCoordinates(targetI, targetJ);
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
	
	// =/({||})\=
	// from spoon

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeInt(targetI);
		output.writeInt(targetJ);
	}

	public ActionTo fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		targetI = input.readInt();
		targetJ = input.readInt();
		return this;
	}

}
