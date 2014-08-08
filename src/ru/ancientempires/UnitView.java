package ru.ancientempires;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.UnitType;

public class UnitView extends SomeWithBitmaps
{
	
	public static Map<UnitType, UnitView>	mapViews	= new HashMap<UnitType, UnitView>();
	
	public static UnitView getView(UnitType unitType)
	{
		return UnitView.mapViews.get(unitType);
	}
	
	public UnitType	type;
	public File		fileImage;
	
	public UnitView(UnitType unitType)
	{
		this.type = unitType;
		UnitView.mapViews.put(unitType, this);
	}
	
}
