package ru.ancientempires;

import ru.ancientempires.helpers.BitmapHelper;
import model.Cell.CellType;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class Cell
{
	
	public Bitmap	bitmap;
	
	public CellType	cellType;
	
	public Cell setBitmap(Resources res, int id)
	{
		this.bitmap = BitmapHelper.getCellBitmap(res, id);
		return this;
	}
	
	public Cell setCellType(CellType cellType)
	{
		this.cellType = cellType;
		return this;
	}
	
}
