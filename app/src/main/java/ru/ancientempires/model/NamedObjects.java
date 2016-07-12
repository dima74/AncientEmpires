package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map.Entry;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;

public class NamedObjects<T>
{

	public HashMap<String, T> objects = new HashMap<>();

	public T get(String name)
	{
		MyAssert.a(objects.containsKey(name));
		return objects.get(name);
	}
	
	public void set(String name, T object)
	{
		objects.put(name, object);
	}

	public JsonArray toJsonPart(T unit)
	{
		JsonArray array = new JsonArray();
		for (Entry<String, T> entry : objects.entrySet())
			if (entry.getValue() == unit)
				array.add(entry.getKey());
		return array;
	}

	public void fromJsonPart(JsonArray names, T unit)
	{
		for (JsonElement name : names)
			objects.put(name.getAsString(), unit);
	}

	public JsonObject toJson()
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
