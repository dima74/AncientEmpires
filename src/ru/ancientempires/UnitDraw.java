package ru.ancientempires;

import model.UnitType;
import android.content.res.Resources;
import java.util.*;

public class UnitDraw extends SomeWithBitmaps
{

	private static HashMap<UnitType, UnitDraw> mapUnitDrawes = new HashMap<UnitType, UnitDraw>();

	public static UnitDraw getUnitDraw(UnitType unitType)
	{
		return mapUnitDrawes.get(unitType);
	}

	public UnitType			unitType;

	public UnitDraw(UnitType unitType)
	{
		this.unitType = unitType;
		mapUnitDrawes.put(unitType, this);
	}

	public static void initResorces(Resources res)
	{
		new UnitDraw(UnitType.getUnitType("SOLDIER")).setBitmaps(res, new int[]{R.drawable.soldier_1, R.drawable.soldier_2});
	}
}
