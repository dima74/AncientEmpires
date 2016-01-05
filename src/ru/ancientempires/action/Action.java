package ru.ancientempires.action;

import java.util.HashMap;
import java.util.Map;

import ru.ancientempires.model.UnitType;

public class Action
{
	
	// public static List<Class<? extends Action>> actions = Arrays.asList();
	
	public ActionType type;
	
	Map<String, Object> properties = new HashMap<String, Object>();
	
	public Action(ActionType type)
	{
		this.type = type;
	}
	
	public Action setProperty(String name, Object property)
	{
		properties.put(name, property);
		return this;
	}
	
	public Object getProperty(String property)
	{
		return properties.get(property);
	}
	
	@Override
	public String toString()
	{
		String s = "";
		if (getProperty("i") != null)
			s += "{" + getProperty("i") + ", " + getProperty("j") + "} ";
		if (getProperty("targetI") != null)
			s += "-> {" + getProperty("targetI") + ", " + getProperty("targetJ") + "} ";
		Integer type = (Integer) getProperty("type");
		if (type != null)
			s += UnitType.getType(type).name + " ";
			
		return s + " " + properties.toString();
	}
	
	// public void saveBase(DataOutputStream output) throws IOException
	// {
	// output.write(Action.actions.indexOf(getClass()));
	// save(output);
	// output.close();
	// }
	
	// public abstract void save(DataOutputStream output);
	
}
