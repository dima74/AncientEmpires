package ru.ancientempires;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;

public class Localization
{
	
	public static String get(String stringID)
	{
		MyAssert.a(Client.client.localization.map.containsKey(stringID));
		return Client.client.localization.map.get(stringID);
	}
	
	public Map<String, String> map = new HashMap<String, String>();
	
	public void loadFull(FileLoader loader) throws IOException
	{
		load(loader.getReader("strings.json"));
		load(getReader(loader));
	}
	
	private void load(JsonReader reader) throws IOException
	{
		reader.beginObject();
		while (reader.peek() == JsonToken.NAME)
			map.put(reader.nextName(), reader.nextString());
		reader.endObject();
		reader.close();
	}
	
	public JsonReader getReader(FileLoader loader) throws IOException
	{
		String prefix = "strings";
		String lang = Locale.getDefault().toString();
		String name = prefix + "_" + lang + ".json";
		String nameDefault = prefix + ".json";
		return loader.getReader(loader.exists(name) ? name : nameDefault);
	}
	
	public String loadName(FileLoader loader) throws IOException
	{
		JsonReader reader = getReader(loader);
		reader.beginObject();
		MyAssert.a("name".equals(reader.nextName()));
		String name = reader.nextString();
		reader.close();
		return name;
	}
	
}
