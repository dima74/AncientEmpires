package ru.ancientempires;

import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import android.graphics.Bitmap;

public class CellBitmap
{
	
	public int		h;
	public int		w;
	
	public boolean	isNormal;
	public int		offsetI;
	public int		offsetJ;
	
	public Bitmap	defaultBitmap;
	public Bitmap	destroyingBitmap;
	public Bitmap[]	colorsBitmaps;
	
	public Bitmap getBitmap(Cell cell)
	{
		CellType type = cell.type;
		if (type.isDestroying && cell.isDestroying)
			return this.destroyingBitmap;
		else if (type.isCapture && cell.isCapture)
			return this.colorsBitmaps[cell.player.ordinal];
		else
			return this.defaultBitmap;
	}
	
	public void changeSize()
	{
		this.h = this.defaultBitmap.getHeight();
		this.w = this.defaultBitmap.getWidth();
	}
}
