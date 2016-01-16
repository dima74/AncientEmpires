package ru.ancientempires;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.FileLoader;

public class Localization
{
	
	public static String get(String stringID)
	{
		// MyAssert.a(map.containsKey(stringID));
		return Client.client.localization.map.get(stringID);
	}
	
	private Map<String, String> map = new HashMap<String, String>();
	
	public void load(FileLoader loader, String prefix) throws IOException
	{
		String lang = Locale.getDefault().toString();
		String name = prefix + "_" + lang + ".json";
		String nameDefault = prefix + ".json";
		JsonReader reader = loader.getReader(loader.exists(name) ? name : nameDefault);
		reader.beginObject();
		while (reader.peek() == JsonToken.NAME)
			map.put(reader.nextName(), reader.nextString());
		reader.endObject();
		reader.close();
	}
	
}
