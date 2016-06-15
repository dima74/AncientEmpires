package ru.ancientempires.images.bitmaps;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.framework.FileLoader;

public class FewBitmaps
{
	
	public static int      ordinal;
	public        Bitmap[] bitmaps;

	public FewBitmaps(Bitmap... bitmaps)
	{
		this.bitmaps = bitmaps;
	}
	
	public FewBitmaps(FileLoader loader, String... names) throws IOException
	{
		bitmaps = new Bitmap[names.length];
		for (int i = 0; i < names.length; i++)
			bitmaps[i] = loader.loadImage(names[i]);
	}
	
	public Bitmap getBitmap()
	{
		return bitmaps[FewBitmaps.ordinal % bitmaps.length];
	}
	
}
