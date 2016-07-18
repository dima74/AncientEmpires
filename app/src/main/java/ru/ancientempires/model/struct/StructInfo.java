package ru.ancientempires.model.struct;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.atteo.classindex.IndexSubclasses;

import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;

@IndexSubclasses
public abstract class StructInfo implements SerializableJson
{

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = ru.ancientempires.serializable.SerializableJsonHelper.toJson(this);
		return object;
	}

	public StructInfo fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		return this;
	}

	static public StructInfo[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		StructInfo[] array = new StructInfo[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = info.fromJson(((com.google.gson.JsonObject) jsonArray.get(i)), StructInfo.class);
		return array;
	}

}
