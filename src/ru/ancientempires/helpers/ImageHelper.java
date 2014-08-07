package ru.ancientempires.helpers;

import model.Cell;
import ru.ancientempires.CellView;
import android.graphics.Bitmap;

public class ImageHelper
{
	
	public static Bitmap getBitmap(Cell cell)
	{
		return CellView.getCellView(cell.cellType).bitmap;
	}
}
