package ru.ancientempires.save;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.FileHelper;
import ru.ancientempires.helpers.JsonHelper;

public class SaveInfo
{
	
	// numberActions / 100 == numberSnapshots
	public int			numberSnapshots;
	public int			numberActions;
	public FileHelper	loader;
	
	public SaveInfo(FileHelper loader)
	{
		this.loader = loader;
	}
	
	public SaveInfo load() throws IOException
	{
		if (!loader.exists("saveInfo.json"))
			return this;
		JsonReader reader = loader.getReader("saveInfo.json");
		reader.beginObject();
		numberSnapshots = JsonHelper.readInt(reader, "numberSnapshots");
		numberActions = JsonHelper.readInt(reader, "numberActions");
		reader.endObject();
		reader.close();
		return this;
	}
	
	public void save() throws IOException
	{
		JsonWriter writer = loader.getWriter("saveInfo.json");
		writer.beginObject();
		writer.name("numberSnapshots").value(numberSnapshots);
		writer.name("numberActions").value(numberActions);
		writer.endObject();
		writer.close();
	}
	
}
