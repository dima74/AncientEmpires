package ru.ancientempires.model;

import ru.ancientempires.model.struct.Struct;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Numbered;

public class CellType implements Numbered
{

	public static CellType newInstance(int i, LoaderInfo info)
	{
		return info.rules.cellTypes[i];
	}

	public int ordinal;

	public CellType baseType;
	public String   name;
	public boolean  isDefault;

	public int steps;
	public int earn;
	public int defense;

	public UnitType[] buyTypes;
	//public Unit[][]   buyUnits;

	public boolean  isHealing;
	public boolean  isCapturing;
	public CellType destroyingType;
	public CellType repairType;

	public CellTemplate template;
	public Struct       struct;

	public int mapEditorFrequency;

	// Эти поля используются только для копирования в клеточку
	public boolean isCaptureDefault;

	public CellType(String name, int ordinal)
	{
		this.name = name.intern();
		this.ordinal = ordinal;
	}

	@Override
	public int getNumber()
	{
		return ordinal;
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
