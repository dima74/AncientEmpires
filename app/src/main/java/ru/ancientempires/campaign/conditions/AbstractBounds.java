package ru.ancientempires.campaign.conditions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;

@IndexSubclasses
public abstract class AbstractBounds implements SerializableJson
{

	public abstract boolean in(int i, int j);

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = ru.ancientempires.serializable.SerializableJsonHelper.toJson(this);
		return object;
	}

	public AbstractBounds fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		return this;
	}

	public static AbstractBounds[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		AbstractBounds[] array = new AbstractBounds[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), AbstractBounds.class);
		return array;
	}

}
