package ru.ancientempires;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.CellType;
import android.graphics.Bitmap;

public class CellView
{
	
	public static Map<CellType, CellView>	mapCellViews	= new HashMap<CellType, CellView>();
	
	public static CellView getCellView(CellType cellType)
	{
		return CellView.mapCellViews.get(cellType);
	}
	
	public CellType	cellType;
	public File		fileImage;
	public Bitmap	bitmap;
	
	public CellView(CellType cellType)
	{
		this.cellType = cellType;
		CellView.mapCellViews.put(cellType, this);
	}
	
}
