package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.serializable.LoaderInfo;

public class PointFrom extends AbstractPoint
{

	public AbstractPoint point;

	public PointFrom() {}

	public PointFrom(Object... point)
	{
		this.point = createPoint(point);
	}

	@Override
	public int getI()
	{
		return point.getI();
	}

	@Override
	public int getJ()
	{
		return point.getJ();
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.add("point", point.toJson());
		return object;
	}

	public PointFrom fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		point = info.fromJson((JsonObject) object.get("point"), AbstractPoint.class);
		return this;
	}

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		point.toData(output);
	}

	public PointFrom fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		point = info.fromData(input, AbstractPoint.class);
		return this;
	}

}
