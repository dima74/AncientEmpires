package ru.ancientempires.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.ancientempires.rules.Rules;

public class CellGroup
{
	
	public String	name;
	public CellType	baseType;
	
	public CellType[]		types;
	public Set<CellType>	typesSet;
	
	public CellGroup(String name, Rules rules)
	{
		this.name = name;
		baseType = rules.getCellType(name + "_GROUP");
		baseType.isDefault = true;
	}
	
	public boolean contains(CellType type)
	{
		return typesSet.contains(type);
	}
	
	public void setTypes(CellType... types)
	{
		this.types = types;
		typesSet = new HashSet<CellType>(Arrays.asList(types));
	}
	
	public void setBaseTypeToAll()
	{
		for (CellType type : types)
			type.setProperties(baseType);
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}