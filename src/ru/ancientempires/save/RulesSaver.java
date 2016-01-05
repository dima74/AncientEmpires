package ru.ancientempires.save;

import java.io.IOException;

import ru.ancientempires.model.CellType;

import com.google.gson.stream.JsonWriter;

public class RulesSaver
{
	
	public static void saveCellTypes(CellType[] types, JsonWriter writer) throws IOException
	{
		writer.beginObject();
		writer.name("all_cell_types").beginArray();
		for (CellType type : types)
			writer.value(type.name);
		writer.endArray();
		writer.endObject();
		writer.close();
	}
}
