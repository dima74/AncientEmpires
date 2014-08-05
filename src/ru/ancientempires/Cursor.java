package ru.ancientempires;

import ru.ancientempires.helpers.BitmapHelper;
import junit.framework.Assert;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class Cursor extends SomeWithBitmaps
{
	
	public int				i;
	public int				j;
	
	private static boolean	isSizeDetermine	= false;
	public static int		height;
	public static int		width;
	
	public boolean			isVisible		= false;
	
	/*@Override
	public FigureTwoBitmaps setBitmapOne(Resources res, int id)
	{
		this.bitmupOne = BitmapHelper.getBitmap(res, id);
		updateSize(this.bitmupOne);
		return this;
	}
	
	@Override
	public FigureTwoBitmaps setBitmapTwo(Resources res, int id)
	{
		this.bitmupTwo = BitmapHelper.getBitmap(res, id);
		updateSize(this.bitmupTwo);
		return this;
	}*/
	
	private void updateSize(Bitmap bitmap)
	{
		if (!Cursor.isSizeDetermine)
		{
			Cursor.height = bitmap.getHeight();
			Cursor.width = bitmap.getWidth();
		}
		// TODO сделать нормальную ошибку
		else if (Cursor.height != bitmap.getHeight() || Cursor.width != bitmap.getWidth()) Assert.assertTrue(false);
	}
}
