package ru.ancientempires.tasks;

import com.google.gson.JsonObject;

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
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
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
