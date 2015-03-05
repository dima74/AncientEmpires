package ru.ancientempires.images.bitmaps;

import ru.ancientempires.model.Unit;
import android.graphics.Bitmap;

public class UnitBitmap
{
	
	public final FewBitmaps	baseBitmap;
	public final Bitmap		textBitmap;
	public final Unit		unit;
	
	public UnitBitmap(FewBitmaps baseBitmaps, Bitmap textBitmap, Unit unit)
	{
		this.baseBitmap = baseBitmaps;
		this.textBitmap = textBitmap;
		this.unit = unit;
	}
	
	public Bitmap getBitmap()
	{
		return this.baseBitmap.getBitmap();
	}
}
