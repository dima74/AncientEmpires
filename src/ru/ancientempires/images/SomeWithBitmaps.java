package ru.ancientempires.images;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public class SomeWithBitmaps
{
	
	public static int	ordinal	= 0;
	
	public Bitmap[]		bitmaps;
	
	public SomeWithBitmaps setAmount(int amount)
	{
		this.bitmaps = new Bitmap[amount];
		return this;
	}
	
	public SomeWithBitmaps setBitmaps(int i, Bitmap bitmap)
	{
		this.bitmaps[i] = bitmap;
		return this;
	}
	
	public SomeWithBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public SomeWithBitmaps setBitmaps(String path, String... names) throws IOException
	{
		this.bitmaps = new Bitmap[names.length];
		for (int i = 0; i < names.length; i++)
			this.bitmaps[i] = BitmapHelper.getResizeBitmap(Client.imagesZipFile, path + names[i]);
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmaps[SomeWithBitmaps.ordinal % this.bitmaps.length];
	}
	
}
