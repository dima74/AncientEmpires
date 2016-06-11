package ru.ancientempires.campaign.coordinates_old2;

public abstract class Coordinate
{

	/*
	public static Class<? extends Coordinate>[]	classes	= new Class[CoordinateType.values().length];
	
	static
	{
		Coordinate.classes[CoordinateType.NAMED_UNIT_I.ordinal()] = CoordinateNamedUnitI.class;
		Coordinate.classes[CoordinateType.NAMED_UNIT_J.ordinal()] = CoordinateNamedUnitJ.class;
	}
	
	public static Coordinate getNew(JsonReader reader, String name) throws IOException
	{
		MyAssert.a(name, reader.nextName());
		if (reader.peek() == JsonToken.NUMBER)
			return new CoordinateInteger(reader.nextInt());
		try
		{
			reader.beginObject();
			CoordinateType type = CoordinateType.valueOf(JsonHelper.readString(reader, "type"));
			Coordinate coordinate = Coordinate.classes[type.ordinal()].newInstance();
			coordinate.load(reader);
			reader.endObject();
			return coordinate;
		}
		catch (IllegalAccessException | InstantiationException e)
		{
			MyAssert.a(false);
			return null;
		}
	}
	
	public abstract void load(JsonReader reader) throws IOException;
	
	public abstract int get();
	
	public final void save(JsonWriter writer, String name) throws IOException
	{
		writer.name(name);
		if (getClass() == CoordinateInteger.class)
			save(writer);
		else
		{
			writer.beginObject();
			writer.name("type").value(getType().name());
			save(writer);
			writer.endObject();
		}
	}
	
	private CoordinateType getType()
	{
		for (CoordinateType type : CoordinateType.values())
			if (Coordinate.classes[type.ordinal()] == getClass())
				return type;
		MyAssert.a(false);
		return null;
	}
	
	public abstract void save(JsonWriter writer) throws IOException;
	//*/
	
}
