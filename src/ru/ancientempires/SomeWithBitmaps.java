package ru.ancientempires;

import ru.ancientempires.helpers.BitmapHelper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SomeWithBitmaps
{
	
	public static int	ordinal	= 0;
	
	public Bitmap[]		bitmaps;
	
	public SomeWithBitmaps setBitmaps(Resources res, int[] ids)
	{
		this.bitmaps = new Bitmap[ids.length];
		for (int i = 0; i < ids.length; i++)
			this.bitmaps[i] = BitmapHelper.getResizeBitmap(BitmapFactory.decodeResource(res, ids[i]));
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmaps[SomeWithBitmaps.ordinal % this.bitmaps.length];
	}
	
}
