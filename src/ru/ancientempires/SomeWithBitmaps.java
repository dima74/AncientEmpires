package ru.ancientempires;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.helpers.BitmapHelper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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
	
	public SomeWithBitmaps setBitmaps(Resources res, int[] ids)
	{
		this.bitmaps = new Bitmap[ids.length];
		for (int i = 0; i < ids.length; i++)
			this.bitmaps[i] = BitmapHelper.getResizeBitmap(BitmapFactory.decodeResource(res, ids[i]));
		return this;
	}
	
	public SomeWithBitmaps setBitmaps(Bitmap[] bitmaps)
	{
		this.bitmaps = bitmaps;
		return this;
	}
	
	public SomeWithBitmaps setBitmaps(String[] names) throws IOException
	{
		this.bitmaps = new Bitmap[names.length];
		for (int i = 0; i < names.length; i++)
			this.bitmaps[i] = BitmapHelper.getResizeBitmap(Client.imagesZipFile, names[i]);
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return this.bitmaps[SomeWithBitmaps.ordinal % this.bitmaps.length];
	}
	
}
