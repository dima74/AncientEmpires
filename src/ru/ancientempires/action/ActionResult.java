package ru.ancientempires.action;

import java.util.HashMap;
import java.util.Map;

public class ActionResult
{
	
	public boolean		successfully	= true;
	
	Map<String, Object>	properties		= new HashMap<String, Object>();
	
	public ActionResult setProperty(String name, Object property)
	{
		this.properties.put(name, property);
		return this;
	}
	
	public Object getProperty(String property)
	{
		return this.properties.get(property);
	}
	
}
