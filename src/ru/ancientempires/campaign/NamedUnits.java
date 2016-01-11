package ru.ancientempires.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ru.ancientempires.client.Client;
import ru.ancientempires.model.Unit;

public class NamedUnits
{
	
	public static NamedUnits get()
	{
		return Client.getGame().namedUnits;
	}
	
	public static Unit get(String name)
	{
		return NamedUnits.get().units.get(name);
	}
	
	public static void set(String name, Unit unit)
	{
		NamedUnits.get().units.put(name, unit);
	}
	
	private Map<String, Unit> units = new HashMap<String, Unit>();
	
	public void tryLoad(DataInputStream input, Unit unit) throws IOException
	{
		int numberNames = input.readInt();
		for (int i = 0; i < numberNames; i++)
		{
			String name = input.readUTF();
			units.put(name, unit);
		}
	}
	
	public void trySave(DataOutputStream output, Unit unit) throws IOException
	{
		ArrayList<String> names = new ArrayList<String>();
		for (Entry<String, Unit> entry : units.entrySet())
			if (entry.getValue() == unit)
				names.add(entry.getKey());
		output.writeInt(names.size());
		for (String name : names)
			output.writeUTF(name);
	}
	
}
