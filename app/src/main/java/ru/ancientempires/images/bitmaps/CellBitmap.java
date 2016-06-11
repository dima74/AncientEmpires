package ru.ancientempires.images.bitmaps;

import ru.ancientempires.images.CellImages;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;

public class CellBitmap
{
	
	public boolean isDual   = false;
	public boolean isSmokes = false;

	public FewBitmaps   defaultBitmap;
	public FewBitmaps[] colorBitmaps;

	public FewBitmaps getBitmap(Cell cell)
	{
		CellType type = cell.type;
		if (type.isCapturing && cell.isCapture())
			return colorBitmaps[CellImages.get().playerToColorI[cell.player.ordinal]];
		else
			return defaultBitmap;
	}
	
}
