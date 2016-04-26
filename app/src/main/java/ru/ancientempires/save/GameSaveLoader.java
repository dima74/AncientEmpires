package ru.ancientempires.save;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.helpers.JsonHelper;

public class GameSaveLoader extends FileLoader
{
	
	volatile public int	numberSnapshots;
	// == numberActions в info.json в папке "actions/%numberSnapshots - 1%"
	volatile public int	numberActionsAfterLastSave;
	
	public GameSaveLoader(FileLoader loader)
	{
		super(loader);
	}
	
	public FileLoader snapshots()
	{
		MyAssert.a(numberSnapshots != 0);
		return getLoader("snapshots/" + (numberSnapshots - 1) + "/");
	}
	
	public FileLoader actions()
	{
		MyAssert.a(numberSnapshots != 0);
		return getLoader("actions/" + (numberSnapshots - 1) + "/");
	}
	
	public GameSaveLoader load() throws IOException
	{
		JsonReader reader = getReader("saveInfo.json");
		reader.beginObject();
		numberSnapshots = JsonHelper.readInt(reader, "numberSnapshots");
		reader.endObject();
		reader.close();
		
		reader = actions().getReader("info.json");
		reader.beginObject();
		numberActionsAfterLastSave = JsonHelper.readInt(reader, "numberActions");
		reader.endObject();
		reader.close();
		return this;
	}
	
	public void save() throws IOException
	{
		JsonWriter writer = getWriter("saveInfo.json");
		writer.beginObject();
		writer.name("numberSnapshots").value(numberSnapshots);
		writer.endObject();
		writer.close();
		
		writer = actions().getWriter("info.json");
		writer.beginObject();
		writer.name("numberActions").value(numberActionsAfterLastSave);
		writer.endObject();
		writer.close();
	}
	
}
