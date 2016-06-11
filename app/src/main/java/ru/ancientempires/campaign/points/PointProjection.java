package ru.ancientempires.campaign.points;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ru.ancientempires.serializable.LoaderInfo;

public class PointProjection extends PointFrom
{

	public Projection projection;

	public PointProjection()
	{
	}

	public PointProjection(Projection projection, Object... point)
	{
		super(point);
		this.projection = projection;
	}

	@Override
	public int getI()
	{
		return game.h * projection.multiH + super.getI() * projection.multiI + projection.offsetI;
	}

	@Override
	public int getJ()
	{
		return game.w * projection.multiW + super.getJ() * projection.multiJ + projection.offsetJ;
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("projection", projection.name());
		return object;
	}

	public PointProjection fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		projection = Projection.valueOf(object.get("projection").getAsString());
		return this;
	}

	public void toData(DataOutputStream output) throws Exception
	{
		super.toData(output);
		output.writeByte(projection.ordinal());
	}

	public PointProjection fromData(DataInputStream input, LoaderInfo info) throws Exception
	{
		super.fromData(input, info);
		projection = projection.values()[input.readByte()];
		return this;
	}

}
