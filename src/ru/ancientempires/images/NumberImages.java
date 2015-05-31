package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import android.graphics.Bitmap;

public abstract class NumberImages
{
	
	public Bitmap[]	numberBitmaps	= new Bitmap[10];
	public Bitmap	minusBitmap;
	public Bitmap	plusBitmap;
	
	public int		h;
	public int		w;
	
	public Bitmap getBitmap(int number)
	{
		return this.numberBitmaps[number];
	}
	
	public void preloadBase(ZipFile images, String path) throws IOException
	{
		for (int i = 0; i < 10; i++)
			this.numberBitmaps[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		this.minusBitmap = BitmapHelper.getResizeBitmap(images, path + "-.png");
		this.h = this.numberBitmaps[0].getHeight();
		this.w = this.numberBitmaps[0].getWidth();
	}
	
}
