package ru.ancientempires.tasks;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.model.Unit;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.WithNumbered;

public abstract class TaskUnitWithValue extends Task
{

	@WithNumbered("numberedUnits")
	public Unit unit;
	public int  value;
	
	public TaskUnitWithValue setUnit(Unit unit)
	{
		this.unit = unit;
		return this;
	}
	
	public TaskUnitWithValue setValue(int value)
	{
		this.value = value;
		return this;
	}
	
	@Override
	public void load(DataInputStream input) throws IOException
	{
		int iUnit = input.readInt();
		unit = game.numberedUnits.get(iUnit);
		value = input.readInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		int iUnit = game.numberedUnits.add(unit);
		output.writeInt(iUnit);
		output.writeInt(value);
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("unit", game.numberedUnits.add(unit));
		object.addProperty("value", value);
		return object;
	}

	public TaskUnitWithValue fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		unit = game.numberedUnits.get(object.get("unit").getAsInt());
		value = object.get("value").getAsInt();
		return this;
	}

}
