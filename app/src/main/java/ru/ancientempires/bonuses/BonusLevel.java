package ru.ancientempires.bonuses;

import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.serializable.LoaderInfo;

public class BonusLevel extends Bonus
{
	
	public int multiAttack;
	public int multiDefence;
	
	public BonusLevel()
	{}
	
	public BonusLevel(int multiAttack, int multiDefence)
	{
		this.multiAttack = multiAttack;
		this.multiDefence = multiDefence;
	}
	
	@Override
	public int getBonusAttack(Game game, Unit unit, Cell cell, Unit targetUnit)
	{
		return unit.level * multiAttack;
	}
	
	@Override
	public int getBonusDefence(Game game, Unit unit, Cell cell, Unit fromUnit)
	{
		return unit.level * multiDefence;
	}
	
	@Override
	public void saveJson(JsonObject object)
	{
		object.addProperty("multiAttack", multiAttack);
		object.addProperty("multiDefence", multiDefence);
	}
	
	@Override
	public void loadJson(JsonObject object, Rules rules)
	{
		multiAttack = object.get("multiAttack").getAsInt();
		multiDefence = object.get("multiDefence").getAsInt();
	}
	
	@Override
	public void save(DataOutputStream output) throws IOException
	{
		output.writeInt(multiAttack);
		output.writeInt(multiDefence);
	}
	
	@Override
	public void load(DataInputStream input, Rules rules) throws IOException
	{
		multiAttack = input.readInt();
		multiDefence = input.readInt();
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + multiAttack;
		result = prime * result + multiDefence;
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
		BonusLevel other = (BonusLevel) obj;
		if (multiAttack != other.multiAttack)
			return false;
		if (multiDefence != other.multiDefence)
			return false;
		return true;
	}
	
	// =/({||})\=
	// from spoon

	public JsonObject toJson() throws Exception
	{
		JsonObject object = super.toJson();
		object.addProperty("multiAttack", multiAttack);
		object.addProperty("multiDefence", multiDefence);
		return object;
	}

	public BonusLevel fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		super.fromJson(object, info);
		multiAttack = object.get("multiAttack").getAsInt();
		multiDefence = object.get("multiDefence").getAsInt();
		return this;
	}

}
