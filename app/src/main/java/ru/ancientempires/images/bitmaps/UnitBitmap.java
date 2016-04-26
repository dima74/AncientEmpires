package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import ru.ancientempires.Point;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.campaign.scripts.ScriptUnitMoveHandler;
import ru.ancientempires.draws.DrawMain;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;

public class UnitBitmap
{
	
	public float y;
	public float x;

	public Unit unit;
	public int health;
	public boolean canUpdateHealth = true;
	public boolean keepTurn = false;
	public ScriptUnitMoveHandler[] handlers;

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
	
	public boolean canUpdatePositiveBonus = true;
	public boolean canUpdateNegativeBonus = true;
	private boolean hasPositiveBonus;
	private boolean hasNegativeBonus;

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
	
	public int idleAnimationFrameLeft;
	
	public void idleAnimation(int frameCount)
	{
		idleAnimationFrameLeft = frameCount;
	}
	
	public void draw(Canvas canvas, int iFrame)
	{
		float y = this.y;
		float x = this.x;
		if (idleAnimationFrameLeft > 0)
		{
			x += (iFrame / 2 % 2 - 1) * 2;
			y += DrawMain.main.rnd.nextBoolean() ? 1 : 0;
			--idleAnimationFrameLeft;
		}
		
		canvas.drawBitmap(getBaseBitmap(), x, y, null);
		Bitmap healthBitmap = getHealthBitmap();
		if (healthBitmap != null)
			canvas.drawBitmap(healthBitmap, x, y + 24 - healthBitmap.getHeight(), null);
		if (unit.level != 0)
		{
			Bitmap levelBitmap = getLevelBitmap();
			canvas.drawBitmap(levelBitmap, x + 24 - levelBitmap.getWidth(), y + 24 - levelBitmap.getHeight(), null);
		}
		
		if (hasPositiveBonus())
		{
			Bitmap bonusBitmap = StatusesImages.get().aura;
			canvas.drawBitmap(bonusBitmap, x, y, null);
		}
		if (hasNegativeBonus())
		{
			Bitmap bonusBitmap = StatusesImages.get().poison;
			canvas.drawBitmap(bonusBitmap, x + 24 - bonusBitmap.getWidth(), y, null);
		}
	}
	
	public Point getIJ()
	{
		return new Point(y / 24, x / 24);
	}
	
	public void move()
	{
		if (handlers != null)
			for (ScriptUnitMoveHandler script : handlers)
				script.unitMove(this);
	}
	
	public boolean exactlyOn(AbstractPoint point)
	{
		return point.getI() * 24 == y && point.getJ() * 24 == x;
	}
	
}
