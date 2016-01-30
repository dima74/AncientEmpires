package ru.ancientempires.model;

public class CellType
{
	
	public int ordinal;
	
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
	
	// Эти поля используются только для копирования в клеточку
	public boolean	isCaptureDefault;
	public boolean	isDestroyDefault;
	
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
		isDestroying = type.isDestroying;
		isHealing = type.isHealing;
		
		isCaptureDefault = type.isCaptureDefault;
		isDestroyDefault = type.isDestroyDefault;
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
