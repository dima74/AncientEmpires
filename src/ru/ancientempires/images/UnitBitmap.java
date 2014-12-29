package ru.ancientempires.images;

import android.graphics.Bitmap;

public class UnitBitmap
{
	
	private final SomeWithBitmaps	someWithBitmaps;
	public final Bitmap				textBitmap;
	
	public UnitBitmap(SomeWithBitmaps someWithBitmaps, Bitmap textBitmap)
	{
		this.someWithBitmaps = someWithBitmaps;
		this.textBitmap = textBitmap;
	}
	
	public Bitmap getBitmap()
	{
		return this.someWithBitmaps.getBitmap();
	}
}
