package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import ru.ancientempires.client.Client;

public class SmallNumberImages extends NumberImages
{
	
	public static SmallNumberImages get()
	{
		return Client.client.images.smallNumber;
	}
	
	public Bitmap defenceBitmap;
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		preloadBase(loader);
		defenceBitmap = loader.loadImage("defence.png");
	}
	
}
