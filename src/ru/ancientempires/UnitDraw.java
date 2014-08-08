package ru.ancientempires;

import java.util.HashMap;

import model.UnitType;
import android.content.res.Resources;

public class UnitDraw extends SomeWithBitmaps
{
	
	private static HashMap<UnitType, UnitDraw>	mapUnitDrawes	= new HashMap<UnitType, UnitDraw>();
	
	public static UnitDraw getUnitDraw(UnitType unitType)
	{
		return UnitDraw.mapUnitDrawes.get(unitType);
	}
	
	public UnitType	unitType;
	
	public UnitDraw(UnitType unitType)
	{
		this.unitType = unitType;
		UnitDraw.mapUnitDrawes.put(unitType, this);
	}
	
	public static void initResorces(Resources res)
	{
		new UnitDraw(UnitType.getType("SOLDIER")).setBitmaps(res, new int[]
		{
				R.drawable.soldier_1, R.drawable.soldier_2
		});
	}
}
