package ru.ancientempires.images;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import ru.ancientempires.helpers.ImagesFileHelper;

public class AndroidImageLoader implements ImagesLoader<Bitmap>
{
	
	@Override
	public Bitmap loadImage(String path) throws IOException
	{
		return BitmapFactory.decodeStream(ImagesFileHelper.openIS(path));
	}
	
}
