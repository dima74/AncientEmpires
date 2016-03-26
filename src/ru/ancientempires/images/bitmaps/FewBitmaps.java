package ru.ancientempires.images.bitmaps;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.images.ImagesLoader;

public class FewBitmaps
{
	
	public static int	ordinal;
	public Bitmap[]		bitmaps;
						
	public FewBitmaps(Bitmap... bitmaps)
	{
		this.bitmaps = bitmaps;
	}
	
	public FewBitmaps(ImagesLoader loader, String... names) throws IOException
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
