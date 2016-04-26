package ru.ancientempires.model;

public class CellType
{

	public int ordinal;

	public CellType baseType;
	public String   name;
	public boolean  isDefault;

	public int steps;
	public int earn;
	public int defense;

	public UnitType[] buyTypes;
	public Unit[][]   buyUnits;

	public boolean  isHealing;
	public boolean  isCapturing;
	public CellType destroyingType;
	public CellType repairType;

	public CellTemplate template;

	// Эти поля используются только для копирования в клеточку
	public boolean isCaptureDefault;

	public CellType(String name, int ordinal)
	{
		this.name = name.intern();
		this.ordinal = ordinal;
	}
	
	public CellType setProperties(CellType type)
	{
		baseType = type;
		steps = type.steps;
		earn = type.earn;
		defense = type.defense;
		buyTypes = type.buyTypes;
		isCapturing = type.isCapturing;
		isHealing = type.isHealing;
		destroyingType = type.destroyingType;
		repairType = type.repairType;
		
		isCaptureDefault = type.isCaptureDefault;
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
