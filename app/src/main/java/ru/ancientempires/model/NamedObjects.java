package ru.ancientempires.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;

public class NamedObjects<T>
{
	
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
		ArrayList<String> names = new ArrayList<>();
		for (Entry<String, T> entry : objects.entrySet())
			if (entry.getValue() == object)
				names.add(entry.getKey());

		output.writeInt(names.size());
		for (String name : names)
			output.writeUTF(name);
	}
	
	public JsonObject toJson() throws Exception
	{
		JsonObject object = new JsonObject();
		for (Entry<String, T> entry : objects.entrySet())
			object.add(entry.getKey(), ((SerializableJson) entry.getValue()).toJson());
		return object;
	}

	public <ActualT extends SerializableJson> void fromJson(JsonObject object, LoaderInfo info, Class<ActualT> c) throws Exception
	{
		for (Entry<String, JsonElement> entry : object.entrySet())
			objects.put(entry.getKey(), (T) info.fromJson((JsonObject) entry.getValue(), c));
	}

}
