package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;
import ru.ancientempires.view.draws.GameDraw;

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
		this.health = unit.health;
		this.y = unit.i * GameDraw.A;
		this.x = unit.j * GameDraw.A;
	}
	
	public Bitmap getBaseBitmap()
	{
		return UnitImages.getUnitBitmap(this.unit, this.keepTurn).getBitmap();
	}
	
	public Bitmap getHealthBitmap()
	{
		if (this.canUpdateHealth)
			this.health = this.unit.health;
		return this.health == 100 ? null : SmallNumberImages.images.getBitmap(this.health);
	}
	
}
