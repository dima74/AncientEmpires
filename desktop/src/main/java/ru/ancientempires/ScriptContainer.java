package ru.ancientempires;

import java.util.ArrayList;
import java.util.List;

import ru.ancientempires.campaign.scripts.Script;

public class ScriptContainer
{
	
	public Script script;
	public ArrayList<ScriptContainer> next = new ArrayList<ScriptContainer>();
	public ArrayList<ScriptContainer> prev = new ArrayList<ScriptContainer>();
	
	public ScriptContainer(Script script)
	{
		this.script = script;
	}
	
	public void add(List<ScriptContainer> containers)
	{
		for (ScriptContainer container : containers)
		{
			next.add(container);
			container.prev.add(this);
		}
	}
	
}
