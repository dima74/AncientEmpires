package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ru.ancientempires.client.Client;
import ru.ancientempires.model.Unit;

public class NumberedUnits
{
	
	public static NumberedUnits get()
	{
		return Client.getGame().numberedUnits;
	}
	
	private ArrayList<Unit> units = new ArrayList<Unit>();
	
	public int add(Unit unit)
	{
		units.add(unit);
		return units.size() - 1;
	}
	
	public Unit get(int iUnit)
	{
		return units.get(iUnit);
	}
	
	public void tryLoad(DataInputStream input, Unit unit) throws IOException
	{
		int numberIndexes = input.readInt();
		for (int i = 0; i < numberIndexes; i++)
		{
			int index = input.readInt();
			while (units.size() <= index)
				units.add(null);
			units.set(index, unit);
		}
	}
	
	public void trySave(DataOutputStream output, Unit unit) throws IOException
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < units.size(); i++)
			if (units.get(i) == unit)
				indexes.add(i);
		output.writeInt(indexes.size());
		for (Integer index : indexes)
			output.writeInt(index);
	}
	
}
