package ru.ancientempires.campaign;

import java.util.HashMap;
import java.util.Map;

import ru.ancientempires.model.Unit;

public class NamedUnits
{
	
	private static Map<String, Unit>	units	= new HashMap<String, Unit>();
	
	public static Unit get(String name)
	{
		return NamedUnits.units.get(name);
	}
	
	public static void set(String name, Unit unit)
	{
		NamedUnits.units.put(name, unit);
	}
	
}
