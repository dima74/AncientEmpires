package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;

public class CellBitmap
{
	
	public boolean		isDual		= false;
	public boolean		isSmokes	= false;
									
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
		if (type.isDestroying && cell.isDestroy)
			return destroyingBitmap;
		else if (type.isCapturing && cell.isCapture())
			return colorBitmaps[CellImages.get().playerToColorI[cell.player.ordinal]];
		else
			return defaultBitmap;
	}
	
}
