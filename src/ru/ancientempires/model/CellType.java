package ru.ancientempires.model;

import java.util.ArrayList;

public class CellType
{
	
	public static int			number;
	public static CellType[]	types;
	
	public static void setCellTypes(String[] typeStrings)
	{
		CellType.number = typeStrings.length;
		CellType.types = new CellType[CellType.number];
		for (int i = 0; i < typeStrings.length; i++)
			CellType.types[i] = new CellType(typeStrings[i], null);
	}
	
	public static void finishInit()
	{
		ArrayList<CellType> staticCellTypes = new ArrayList<CellType>();
		for (int i = 0; i < CellType.number; i++)
		{
			CellType type = CellType.types[i];
			type.ordinal = i;
			if (type.isStatic)
				staticCellTypes.add(type);
		}
		
		for (int i = 0; i < staticCellTypes.size(); i++)
			staticCellTypes.get(i).staticOrdinal = i;
		Cell.setStaticCells(staticCellTypes.toArray(new CellType[0]));
	}
	
	public static CellType getType(String name)
	{
		for (CellType cellType : CellType.types)
			if (cellType.name.equals(name))
				return cellType;
		return null;
	}
	
	public static CellType getType(int ordinal)
	{
		return CellType.types[ordinal];
	}
	
	public String			name;
	public int				ordinal;
	public CellTypeGroup	typeGroup;
	
	public boolean	isStatic;
	// Для нестатичных клеточек всегда равно -1
	public int		staticOrdinal	= -1;
	
	public int					baseSteps;
	public int					earn;
	public int					defense;
	public boolean				isCapture;
	public boolean				isDestroying;
	public boolean				isHeal;
	public ArrayList<UnitType>	buyUnitsDefault;
	public ArrayList<Unit>[]	buyUnits;
	
	public CellTypeGroup group;
	
	public CellType()
	{}
	
	public CellType(String name, CellType type)
	{
		this.name = name.intern();
		if (type != null)
			setProperties(type);
	}
	
	public CellType setProperties(CellType type)
	{
		this.isStatic = type.isStatic;
		this.baseSteps = type.baseSteps;
		this.earn = type.earn;
		this.defense = type.defense;
		this.isCapture = type.isCapture;
		this.isDestroying = type.isDestroying;
		this.isHeal = type.isHeal;
		this.buyUnitsDefault = type.buyUnitsDefault;
		
		this.group = type.group;
		return this;
	}
	
	@Override
	public String toString()
	{
		return this.name;
	}
	
	// @Override
	public String toString1()
	{
		return "CellType "
				+ "[name=" + this.name
				+ ", ordinal=" + this.ordinal
				+ ", typeGroup=" + this.typeGroup
				+ ", isStatic=" + this.isStatic
				+ ", staticOrdinal=" + this.staticOrdinal
				+ ", baseDifficulte=" + this.baseSteps
				+ "]";
	}
	
}
