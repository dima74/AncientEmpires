package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class NumberedObjects<T> {

	public ArrayList<T> objects = new ArrayList<>();

	public int add(T object) {
		objects.add(object);
		return objects.size() - 1;
	}

	public T get(int i) {
		return i < objects.size() ? objects.get(i) : null;
	}

	public void toJsonPart(JsonObject json, T object) {
		JsonArray array = new JsonArray();
		for (int i = 0; i < objects.size(); i++)
			if (objects.get(i) == object)
				array.add(i);
		if (array.size() > 0)
			json.add("indexes", array);
	}

	public void fromJsonPart(JsonObject json, T object) {
		JsonArray indexes = (JsonArray) json.get("indexes");
		if (indexes == null)
			return;
		for (JsonElement indexJson : indexes) {
			int index = indexJson.getAsInt();
			while (objects.size() <= index)
				objects.add(null);
			objects.set(index, object);
		}
	}

}
