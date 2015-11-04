package ru.ancientempires.images.bitmaps;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.BitmapHelper;

public class FewBitmaps
{
	
	public static int	ordinal	= 0;
	public Bitmap[]		bitmaps;
	
	public FewBitmaps setAmount(int amount)
	{
		this.bitmaps = new Bitmap[amount];
		return this;
	}
	
	public FewBitmaps setBitmaps(int i, Bitmap bitmap)
	{
		this.bitmaps[i] = bitmap;
		return this;
	}
	
	public FewBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public FewBitmaps setBitmaps(String path, String... names) throws IOException
	{
		this.bitmaps = new Bitmap[names.length];
		for (int i = 0; i < names.length; i++)
			this.bitmaps[i] = BitmapHelper.getResizeBitmap(Client.imagesZipFile, path + names[i]);
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmaps[FewBitmaps.ordinal % this.bitmaps.length];
	}
	
}
