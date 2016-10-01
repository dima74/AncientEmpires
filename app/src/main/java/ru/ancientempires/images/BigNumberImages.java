package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.framework.FileLoader;

public class BigNumberImages extends NumberImages
{

	public Bitmap slashBitmap;

	@Override
	public void preload(FileLoader loader) throws IOException
	{
		plus = loader.loadImage("+.png");
		slashBitmap = loader.loadImage("slash.png");
		preloadBase(loader);
	}

}
