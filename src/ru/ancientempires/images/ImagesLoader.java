package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.ancientempires.helpers.FileLoader;

public class ImagesLoader extends FileLoader
{
	
	public ImagesLoader(FileLoader fileHelper)
	{
		super(fileHelper);
	}
	
	public ImagesLoader(FileLoader loader, String prefix)
	{
		super(loader, prefix);
	}
	
	public Bitmap loadImage(String name) throws IOException
	{
		return BitmapFactory.decodeStream(openIS(name));
	}
	
	public Bitmap loadImageAndResize(String name, float scale) throws IOException
	{
		Bitmap bitmap = loadImage(name);
		int width = (int) (bitmap.getWidth() * scale);
		int height = (int) (bitmap.getHeight() * scale);
		return Bitmap.createScaledBitmap(bitmap, width, height, false);
	}
	
	public ImagesLoader getImagesLoader(String prefix)
	{
		return new ImagesLoader(this, prefix);
	}
	
}
