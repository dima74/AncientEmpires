package ru.ancientempires.campaign;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;

public class NamedObjects<T>
{
	
	public static NamedObjects get()
	{
		return Client.getGame().namedUnits;
	}
	
	public T get(String name)
	{
		MyAssert.a(objects.containsKey(name));
		return objects.get(name);
	}
	
	public void set(String name, T object)
	{
		objects.put(name, object);
	}
	
	public HashMap<String, T> objects = new HashMap<>();
	
	public void tryLoad(DataInputStream input, T object) throws IOException
	{
		int numberNames = input.readInt();
		for (int i = 0; i < numberNames; i++)
		{
			String name = input.readUTF();
			objects.put(name, object);
		}
	}
	
	public void trySave(DataOutputStream output, T object) throws IOException
	{
		ArrayList<String> names = new ArrayList<String>();
		for (Entry<String, T> entry : objects.entrySet())
			if (entry.getValue() == object)
				names.add(entry.getKey());

		output.writeInt(names.size());
		for (String name : names)
			output.writeUTF(name);
	}
	
}
