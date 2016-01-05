package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBitmap
{
	
	public float	y;
	public float	x;
	
	public Unit		unit;
	public int		health;
	public boolean	canUpdateHealth	= true;
	public boolean	keepTurn		= false;
	
	public UnitBitmap(Unit unit)
	{
		this.unit = unit;
		health = unit.health;
		y = unit.i * 24;
		x = unit.j * 24;
	}
	
	public Bitmap getBaseBitmap()
	{
		return UnitImages.get().getUnitBitmap(unit, keepTurn).getBitmap();
	}
	
	public Bitmap getHealthBitmap()
	{
		if (canUpdateHealth)
			health = unit.health;
		return health == 100 ? null : SmallNumberImages.get().getBitmap(health);
	}
	
}
