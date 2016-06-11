package ru.ancientempires.model.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.model.Cell;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;

@IndexSubclasses
public abstract class Struct implements SerializableJson
{

	public static Struct fromJSON(JsonElement element, LoaderInfo info)
	{
		Struct struct = new StructCitadel();
		struct.loadJSON(element, info);
		return struct;
	}

	public abstract void loadJSON(JsonElement element, LoaderInfo info);

	public abstract JsonElement toJSON();

	public abstract boolean canActivate(Cell cell);

	public abstract void activate(Cell cell);

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = SerializableJsonHelper.toJson(this);
		return object;
	}

	public Struct fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		return this;
	}

	static public Struct[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Struct[] array = new Struct[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), Struct.class);
		return array;
	}

}
