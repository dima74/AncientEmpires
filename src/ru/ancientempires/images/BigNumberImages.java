package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;

public class BigNumberImages extends NumberImages
{
	
	public Bitmap slashBitmap;
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		plus = loader.loadImage("+.png");
		slashBitmap = loader.loadImage("slash.png");
		preloadBase(loader);
	}
	
}
