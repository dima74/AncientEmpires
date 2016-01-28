package ru.ancientempires.model;

public class CellType
{
	
	public CellType	baseType;
	public String	name;
	public boolean	isDefault;
	
	public int	steps;
	public int	earn;
	public int	defense;
	
	public UnitType[]	buyTypes;
	public Unit[][]		buyUnits;
	
	public boolean	isCapturing;
	public boolean	isDestroying;
	public boolean	isHealing;
	public boolean	isStatic;
	
	public CellType(String name)
	{
		this.name = name;
	}
	
	public CellType setProperties(CellType type)
	{
		baseType = type;
		steps = type.steps;
		earn = type.earn;
		defense = type.defense;
		buyTypes = type.buyTypes;
		isCapturing = type.isCapturing;
		isDestroying = type.isDestroying;
		isHealing = type.isHealing;
		isStatic = type.isStatic;
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
