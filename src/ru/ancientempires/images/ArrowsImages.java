package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;

public class ArrowsImages extends IImages
{
	
	public Bitmap	equals;
	public Bitmap	more;
	public Bitmap	less;
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		equals = loader.loadImage("equals.png");
		more = loader.loadImage("more.png");
		less = loader.loadImage("less.png");
	}
	
}
