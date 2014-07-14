package ru.ancientempires;

import ru.ancientempires.helpers.BitmapHelper;
import android.content.res.Resources;
import android.graphics.Bitmap;

public abstract class FigureTwoBitmaps
{
	
	public Bitmap	bitmupOne;
	public Bitmap	bitmupTwo;
	
	public FigureTwoBitmaps setBitmapOne(Resources res, int id)
	{
		this.bitmupOne = BitmapHelper.getCellBitmap(res, id);
		return this;
	}
	
	public FigureTwoBitmaps setBitmapTwo(Resources res, int id)
	{
		this.bitmupTwo = BitmapHelper.getCellBitmap(res, id);
		return this;
	}
}
