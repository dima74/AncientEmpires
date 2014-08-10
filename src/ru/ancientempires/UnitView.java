package ru.ancientempires;

import java.io.File;

import model.UnitType;

public class UnitView extends SomeWithBitmaps
{
	
	// public static Map<UnitType, UnitView> mapViews = new HashMap<UnitType, UnitView>();
	private static UnitView[]	views;
	
	public static UnitView getView(UnitType unitType)
	{
		for (UnitView unitView : UnitView.views)
			if (unitView.type.equals(unitType))
				return unitView;
		return null;
	}
	
	public UnitType		type;
	public static int	count;
	public static int	currOrdinal;
	
	public static void setCount(int count)
	{
		UnitView.count = count;
		UnitView.currOrdinal = 0;
		UnitView.views = new UnitView[count];
	}
	
	public File	fileImage;
	public int	ordinal;
	
	public UnitView(UnitType unitType)
	{
		this.type = unitType;
		
		this.ordinal = UnitView.currOrdinal;
		UnitView.views[this.ordinal] = this;
		UnitView.currOrdinal++;
	}
	
}
