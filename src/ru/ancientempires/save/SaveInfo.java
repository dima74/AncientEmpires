package ru.ancientempires.save;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.helpers.JsonHelper;

public class SaveInfo
{
	
	public int				numberSnapshots;
	// == numberActions в info.json в папке "actions/%numberSnapshots - 1%"
	public int				numberActionsAfterLastSave;
	public GameSaveLoader	loader;
	
	public SaveInfo load() throws IOException
	{
		if (!loader.exists("saveInfo.json"))
			return this;
		JsonReader reader = loader.getReader("saveInfo.json");
		reader.beginObject();
		numberSnapshots = JsonHelper.readInt(reader, "numberSnapshots");
		reader.endObject();
		reader.close();
		
		reader = loader.actions().getReader("info.json");
		reader.beginObject();
		numberActionsAfterLastSave = JsonHelper.readInt(reader, "numberActions");
		reader.endObject();
		reader.close();
		return this;
	}
	
	public void save() throws IOException
	{
		JsonWriter writer = loader.getWriter("saveInfo.json");
		writer.beginObject();
		writer.name("numberSnapshots").value(numberSnapshots);
		writer.endObject();
		writer.close();
		
		writer = loader.actions().getWriter("info.json");
		writer.beginObject();
		writer.name("numberActions").value(numberActionsAfterLastSave);
		writer.endObject();
		writer.close();
	}
	
}
