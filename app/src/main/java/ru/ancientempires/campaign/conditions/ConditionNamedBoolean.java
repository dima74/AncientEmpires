package ru.ancientempires.campaign.conditions;

public class ConditionNamedBoolean extends Condition
{

	public String name;

	public ConditionNamedBoolean()
	{}

	public ConditionNamedBoolean(String name)
	{
		this.name = name;
	}

	@Override
	public boolean check()
	{
		return game.namedBooleans.get(name);
	}

}
