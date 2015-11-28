package ru.ancientempires.images.bitmaps;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;

public class FewBitmaps
{
	
	public static int	ordinal	= 0;
	public Bitmap[]		bitmaps;
	
	public FewBitmaps setAmount(int amount)
	{
		bitmaps = new Bitmap[amount];
		return this;
	}
	
	public FewBitmaps setBitmaps(int i, Bitmap bitmap)
	{
		bitmaps[i] = bitmap;
		return this;
	}
	
	public FewBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public FewBitmaps setBitmaps(String path, String... names) throws IOException
	{
		bitmaps = new Bitmap[names.length];
		for (int i = 0; i < names.length; i++)
			bitmaps[i] = BitmapHelper.getResizeBitmap(path + names[i]);
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return bitmaps[FewBitmaps.ordinal % bitmaps.length];
	}
	
}
