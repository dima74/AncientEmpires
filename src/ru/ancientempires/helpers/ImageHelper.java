package ru.ancientempires.helpers;

import model.Cell;
import model.Unit;
import ru.ancientempires.CellView;
import ru.ancientempires.UnitView;
import android.graphics.Bitmap;

public class ImageHelper
{
	
	public static Bitmap getCellBitmap(Cell cell)
	{
		return CellView.getView(cell.type).bitmap;
	}
	
	public static Bitmap getUnitBitmap(Unit unit)
	{
		// unit.UnitType и unit.color -> getNewImg -> Bitmap, но все это делать надо при старте игры.
		return UnitView.getView(unit.type).getBitmap();
	}
	
}
