package ru.ancientempires.campaign.scripts;

import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import ru.ancientempires.Point;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.images.bitmaps.UnitBitmap;

public class ScriptUnitMoveHandlerPoint extends ScriptUnitMoveHandler
{
	
	public Point point;
	
	public ScriptUnitMoveHandlerPoint()
	{}
	
	public ScriptUnitMoveHandlerPoint(int i, int j)
	{
		point = new Point(i, j);
	}
	
	@Override
	public void unitMove(UnitBitmap unit)
	{
		if (unit.exactlyOn(point))
			complete();
	}
	
	@Override
	public void load(JsonReader reader) throws IOException
	{
		int i = JsonHelper.readInt(reader, "i");
		int j = JsonHelper.readInt(reader, "j");
		point = new Point(i, j);
	}
	
	@Override
	public void save(JsonWriter writer) throws IOException
	{
		writer.name("i").value(point.i);
		writer.name("j").value(point.j);
	}
	
}
