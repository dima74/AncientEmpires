package ru.ancientempires.serializable;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SerializableJsonHelper
{
	
	public static JsonObject toJson(SerializableJson object)
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", SerializableHelper.saveMap.get(object.getClass()).index);
		jsonObject.addProperty("typeName", object.getClass().getSimpleName());
		return jsonObject;
	}
	
	public static <T extends SerializableJson> JsonArray toJsonArray(Iterable<T> array)
	{
		JsonArray jsonArray = new JsonArray();
		for (SerializableJson object : array)
			jsonArray.add(object.toJson());
		return jsonArray;
	}
	
	public static <T extends SerializableJson> JsonArray toJsonArray(T[] array)
	{
		return toJsonArray(Arrays.asList(array));
	}
	
	public static <T extends Named> JsonArray toJsonArrayNamed(Iterable<T> array)
	{
		JsonArray jsonArray = new JsonArray();
		for (Named object : array)
			jsonArray.add(object.getName());
		return jsonArray;
	}
	
	public static <T extends Named> JsonArray toJsonArrayNamed(T[] array)
	{
		return toJsonArrayNamed(Arrays.asList(array));
	}
	
	public static <T extends Numbered> JsonArray toJsonArrayNumbered(Iterable<T> array)
	{
		JsonArray jsonArray = new JsonArray();
		for (Numbered object : array)
			jsonArray.add(object.getNumber());
		return jsonArray;
	}
	
	public static <T extends Numbered> JsonArray toJsonArrayNumbered(T[] array)
	{
		return toJsonArrayNumbered(Arrays.asList(array));
	}
	
	public static boolean equals(SerializableJson a, SerializableJson b)
	{
		return a.toJson().equals(b.toJson());
	}
	
	public static int hashCode(SerializableJson o)
	{
		return o.toJson().hashCode();
	}
	
	public static void eraseDefaults(JsonObject object, JsonObject defaultObject)
	{
		for (Map.Entry<String, JsonElement> entry : defaultObject.entrySet())
			if (entry.getValue().equals(object.get(entry.getKey())))
				object.remove(entry.getKey());
	}
	
	public static JsonObject insertDefaults(JsonObject object, JsonObject defaultObject)
	{
		for (Map.Entry<String, JsonElement> entry : defaultObject.entrySet())
		{
			String key = entry.getKey();
			JsonElement value = object.get(key);
			if (value == null || value == JsonNull.INSTANCE)
				object.add(key, entry.getValue());
		}
		return object;
	}
	
	public static JsonArray insertDefaults(JsonArray array, JsonArray defaultsArray)
	{
		if (array == null)
		{
			array = new JsonArray();
			for (int i = 0; i < defaultsArray.size(); i++)
				array.add(new JsonObject());
		}
		for (int i = 0; i < defaultsArray.size(); i++)
			insertDefaults((JsonObject) array.get(i), (JsonObject) defaultsArray.get(i));
		return array;
	}
	
	public static JsonObject leaveOnly(JsonObject object, String... keys)
	{
		JsonObject result = new JsonObject();
		for (String key : keys)
			result.add(key, object.get(key));
		return result;
	}

	public static JsonObject eraseNulls(JsonObject object)
	{
		ArrayList<String> keys = new ArrayList<>();
		for (Map.Entry<String, JsonElement> entry : object.entrySet())
			if (entry.getValue() == JsonNull.INSTANCE)
				keys.add(entry.getKey());
		for (String key : keys)
			object.remove(key);
		return object;
	}

	public static JsonObject deepCopy(JsonObject object)
	{
		return (JsonObject) new JsonParser().parse(object.toString());
	}

	public static String diff(SerializableJson one, SerializableJson two)
	{
		return diff(one.toJson(), two.toJson());
	}

	public static String diff(JsonObject one, JsonObject two)
	{
		String a = toJsonPretty(one);
		String b = toJsonPretty(two);
		String[] as = a.split("\n");
		String[] bs = b.split("\n");
		int n = Math.min(as.length, bs.length);
		for (int i = 0; i < n; i++)
			if (!as[i].equals(bs[i]))
			{
				String result = "";
				int x = 15;
				for (int j = -x; j <= x; j++)
					if (0 <= i + j && i + j < as.length)
						result += as[i + j] + "\n";
				result += "\n=====================\n\n";
				for (int j = -x; j <= x; j++)
					if (0 <= i + j && i + j < bs.length)
						result += bs[i + j] + "\n";
				return result;
			}
		return String.format("%d %d", as.length, bs.length);
	}

	public static String toJsonPretty(SerializableJson object)
	{
		return toJsonPretty(object.toJson());
	}

	public static String toJsonPretty(JsonObject object)
	{
		return new GsonBuilder().setPrettyPrinting().create().toJson(object);
	}

}
