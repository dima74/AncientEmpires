package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NumberedObjects<T>
{
	
	public ArrayList<T> objects = new ArrayList<>();
	
	public int add(T object)
	{
		objects.add(object);
		return objects.size() - 1;
	}
	
	public T get(int i)
	{
		return i < objects.size() ? objects.get(i) : null;
	}

	// TODO public void toJsonPart(JsonObject json, T object)
	public JsonArray toJsonPart(T object)
	{
		JsonArray array = new JsonArray();
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) == object)
				array.add(i);
		return array;
	}

	public void fromJsonPart(JsonArray indexes, T object)
	{
		if (indexes == null)
			return;
		for (JsonElement indexJson : indexes)
		{
			int index = indexJson.getAsInt();
			while (objects.size() <= index)
				objects.add(null);
			objects.set(index, object);
		}
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
		ArrayList<Integer> indexes = new ArrayList<>();
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) == object)
				indexes.add(i);
		output.writeInt(indexes.size());
		for (Integer index : indexes)
			output.writeInt(index);
	}
	
}
