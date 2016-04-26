package ru.ancientempires.campaign.scripts;

public class ScriptSetNamedBoolean extends Script
{

	public String  name;
	public boolean bool;

	public ScriptSetNamedBoolean()
	{}

	public ScriptSetNamedBoolean(String name, boolean bool)
	{
		this.name = name;
		this.bool = bool;
	}

	@Override
	public void start()
	{
		game.namedBooleans.set(name, bool);
	}

}
