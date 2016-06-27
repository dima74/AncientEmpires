package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.serializable.LoaderInfo;

public class PointOffset extends PointFrom
{

	public int offsetI;
	public int offsetJ;

	public PointOffset() {}

	public PointOffset(int offsetI, int offsetJ, Object... point)
	{
		super(point);
		this.offsetI = offsetI;
		this.offsetJ = offsetJ;
	}

	@Override
	public int getI()
	{
		return super.getI() + offsetI;
	}

	@Override
	public int getJ()
	{
		return super.getJ() + offsetJ;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = super.toJson();
		object.addProperty("offsetI", offsetI);
		object.addProperty("offsetJ", offsetJ);
		return object;
	}

	public PointOffset fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		offsetI = object.get("offsetI").getAsInt();
		offsetJ = object.get("offsetJ").getAsInt();
		return this;
	}

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeInt(offsetI);
		output.writeInt(offsetJ);
	}

	public PointOffset fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		offsetI = input.readInt();
		offsetJ = input.readInt();
		return this;
	}

}
