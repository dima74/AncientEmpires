package ru.ancientempires;

import model.Cell;
import model.CellType;
import android.graphics.Bitmap;

public class CellBitmap
{
	
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
}
