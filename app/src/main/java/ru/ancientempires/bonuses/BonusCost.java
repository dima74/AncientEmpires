package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusCost extends Bonus
{
	
	public int multi;
	
	public BonusCost()
	{}
	
	public BonusCost(int multi)
	{
		this.multi = multi;
	}
	
	@Override
	public int getBonusCost(Game game, Unit unit)
	{
		return unit.numberBuys * multi;
	}
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("multi", multi);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		multi = object.get("multi").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeInt(multi);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		multi = input.readInt();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + multi;
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BonusCost other = (BonusCost) obj;
		if (multi != other.multi)
			return false;
		return true;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("multi", multi);
		return object;
	}

	public BonusCost fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		multi = object.get("multi").getAsInt();
		return this;
	}

}
