package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.images.*;

public class CellBitmap
{
	
	public boolean	isDual		= false;
	public boolean	isSmokes	= false;
	
	public FewBitmaps	defaultBitmap;
	public FewBitmaps	destroyingBitmap;
	public FewBitmaps[]	colorBitmaps;
	
	public Bitmap getBitmap(Cell cell)
	{
		return getFewBitmaps(cell).getBitmap();
	}
	
	private FewBitmaps getFewBitmaps(Cell cell)
	{
		CellType type = cell.type;
		if (type.isDestroying && cell.isDestroying)
			return this.destroyingBitmap;
		else if (type.isCapture && cell.isCapture)
			return this.colorBitmaps[CellImages.playerToColorI[cell.player.ordinal]];
		else
			return this.defaultBitmap;
	}
	
}
