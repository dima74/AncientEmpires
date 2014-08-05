package ru.ancientempires;

import model.UnitType;
import android.content.res.Resources;
import java.util.*;

public class UnitDraw1 extends FigureTwoBitmaps
{
	
	//public static Unit		soldier;
	
	//public static UnitDraw[]	units;
	
	private static HashMap<UnitType, UnitDraw1> mapUnitDrawes = new HashMap<UnitType, UnitDraw1>();
	
	public static UnitDraw1 getUnitDraw(UnitType unitType)
	{
		return mapUnitDrawes.get(unitType);
	}
	
	public UnitType			unitType;
	
	public UnitDraw1(UnitType unitType)
	{
		this.unitType = unitType;
		mapUnitDrawes.put(unitType, this);
	}

	public static UnitDraw1 getUnit(UnitType unitType)
	{
		return mapUnitDrawes.get(unitType);
	}
	
	public static void initResorces(Resources res)
	{
		new UnitDraw1(UnitType.getUnitType("SOLDIER")).setBitmapOne(res, R.drawable.soldier_1).setBitmapTwo(res, R.drawable.soldier_2);
		
		/*units = new UnitDraw[]
		{
				soldier
		};*/
	}
}
