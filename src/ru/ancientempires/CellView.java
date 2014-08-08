package ru.ancientempires;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.CellType;
import android.graphics.Bitmap;

public class CellView
{
	
	public static Map<CellType, CellView>	mapViews	= new HashMap<CellType, CellView>();
	
	public static CellView getView(CellType cellType)
	{
		return CellView.mapViews.get(cellType);
	}
	
	public CellType	type;
	public File		fileImage;
	public Bitmap	bitmap;
	
	public CellView(CellType cellType)
	{
		this.type = cellType;
		CellView.mapViews.put(cellType, this);
	}
	
}
