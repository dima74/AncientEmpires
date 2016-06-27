package ru.ancientempires.model.struct;

import com.google.gson.JsonObject;

import ru.ancientempires.serializable.LoaderInfo;

public class StructInfoCitadel extends StructInfo
{

	public int crystalsReceived;

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("crystalsReceived", crystalsReceived);
		return object;
	}

	public StructInfoCitadel fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		crystalsReceived = object.get("crystalsReceived").getAsInt();
		return this;
	}

}
