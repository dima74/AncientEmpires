package ru.ancientempires.tasks;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public abstract class TaskFrom extends Task
{
	
	public int i;
	public int j;
	
	public TaskFrom setIJ(int i, int j)
	{
		this.i = i;
		this.j = j;
		return this;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("i", i);
		object.addProperty("j", j);
		return object;
	}

	public TaskFrom fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		return this;
	}

}
