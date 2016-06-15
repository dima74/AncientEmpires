package ru.ancientempires.serializable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class SerializableJsonHelper
{

	public static JsonObject toJson(SerializableJson object) throws Exception
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", SerializableHelper.saveMap.get(object.getClass()).index);
		jsonObject.addProperty("typeName", object.getClass().getSimpleName());
		return jsonObject;
	}

	public static <T extends SerializableJson> JsonArray toJsonArray(Iterable<T> array) throws Exception
	{
		JsonArray jsonArray = new JsonArray();
		for (SerializableJson object : array)
			jsonArray.add(object.toJson());
		return jsonArray;
	}

	public static <T extends SerializableJson> JsonArray toJsonArray(T[] array) throws Exception
	{
		return toJsonArray(Arrays.asList(array));
	}

	public static <T extends Named> JsonArray toJsonArrayNamed(Iterable<T> array) throws Exception
	{
		JsonArray jsonArray = new JsonArray();
		for (Named object : array)
			jsonArray.add(object.getName());
		return jsonArray;
	}

	public static <T extends Named> JsonArray toJsonArrayNamed(T[] array) throws Exception
	{
		return toJsonArrayNamed(Arrays.asList(array));
	}

	public static <T extends Numbered> JsonArray toJsonArrayNumbered(Iterable<T> array) throws Exception
	{
		JsonArray jsonArray = new JsonArray();
		for (Numbered object : array)
			jsonArray.add(object.getNumber());
		return jsonArray;
	}

	public static <T extends Numbered> JsonArray toJsonArrayNumbered(T[] array) throws Exception
	{
		return toJsonArrayNumbered(Arrays.asList(array));
	}

}
