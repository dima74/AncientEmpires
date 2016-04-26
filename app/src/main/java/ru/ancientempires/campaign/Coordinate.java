
package ru.ancientempires.campaign;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.framework.MyAssert;

public abstract class Coordinate
{
	
	public static Coordinate getNew(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		if (reader.peek() == JsonToken.NUMBER)
			return new CoordinateInteger(reader.nextInt());
		else
			return new CoordinateName(reader.nextString());
	}
	
	public abstract int get();
	
	public void save(JsonWriter writer, String name) throws IOException
	{
		writer.name(name);
		save(writer);
	}
	
	public abstract void save(JsonWriter writer) throws IOException;
	
}
