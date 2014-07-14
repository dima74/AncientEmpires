package ru.ancientempires;

import model.Unit.UnitType;
import android.content.res.Resources;

public class Unit extends FigureTwoBitmaps
{
	
	public static Unit		soldier;
	
	public static Unit[]	units;
	
	public UnitType			unitType;
	
	public Unit(UnitType unitType)
	{
		this.unitType = unitType;
	}
	
	public static void initResorces(Resources res)
	{
		soldier = (Unit) new Unit(UnitType.SOLDIER).setBitmapOne(res, R.drawable.soldier_1).setBitmapTwo(res, R.drawable.soldier_2);
		
		units = new Unit[]
		{
				soldier
		};
	}
}
