package ru.ancientempires.tasks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Numbered<T>
{
	
	private ArrayList<T> objects = new ArrayList<T>();
	
	public int add(T object)
	{
		objects.add(object);
		return objects.size() - 1;
	}
	
	public T get(int i)
	{
		return i < objects.size() ? objects.get(i) : null;
	}
	
	public void tryLoad(DataInputStream input, T object) throws IOException
	{
		int numberIndexes = input.readInt();
		for (int i = 0; i < numberIndexes; i++)
		{
			int index = input.readInt();
			while (objects.size() <= index)
				objects.add(null);
			objects.set(index, object);
		}
	}
	
	public void trySave(DataOutputStream output, T object) throws IOException
	{
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) == object)
				indexes.add(i);
		output.writeInt(indexes.size());
		for (Integer index : indexes)
			output.writeInt(index);
	}
	
}
