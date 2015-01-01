package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;

public class UnitBitmap
{
	
	public final FewBitmaps	baseBitmap;
	public final Bitmap		textBitmap;
	
	public UnitBitmap(FewBitmaps baseBitmaps, Bitmap textBitmap)
	{
		this.baseBitmap = baseBitmaps;
		this.textBitmap = textBitmap;
	}
	
	public Bitmap getBitmap()
	{
		return this.baseBitmap.getBitmap();
	}
}
