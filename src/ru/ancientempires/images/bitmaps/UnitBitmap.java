package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import ru.ancientempires.Point;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveHandler;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBitmap
{
	
	public float					y;
	public float					x;
									
	public Unit						unit;
	public int						health;
	public boolean					canUpdateHealth	= true;
	public boolean					keepTurn		= false;
	public ScriptUnitMoveHandler[]	hanlers;
									
	public UnitBitmap(Unit unit)
	{
		this(unit, unit.i, unit.j);
	}
	
	public UnitBitmap(Unit unit, int i, int j)
	{
		this.unit = unit;
		health = unit.health;
		y = i * 24;
		x = j * 24;
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
	
	public Bitmap getLevelBitmap()
	{
		return unit.level < 10
				? SmallNumberImages.get().getBitmap(unit.level)
				: SmallNumberImages.get().asterisk;
	}
	
	public boolean	canUpdatePositiveBonus	= true;
	public boolean	canUpdateNegativeBonus	= true;
	private boolean	hasPositiveBonus;
	private boolean	hasNegativeBonus;
					
	public boolean hasPositiveBonus()
	{
		if (canUpdatePositiveBonus)
			hasPositiveBonus = unit.hasPositiveBonus();
		return hasPositiveBonus;
	}
	
	public boolean hasNegativeBonus()
	{
		if (canUpdateNegativeBonus)
			hasNegativeBonus = unit.hasNegativeBonus();
		return hasNegativeBonus;
	}
	
	public Point getIJ()
	{
		return new Point(y / 24, x / 24);
	}
	
	public void move()
	{
		if (hanlers != null)
			for (ScriptUnitMoveHandler script : hanlers)
				script.unitMove(this);
	}
	
	public boolean exactlyOn(Point point)
	{
		return point.i * 24 == y && point.j * 24 == x;
	}
	
}
