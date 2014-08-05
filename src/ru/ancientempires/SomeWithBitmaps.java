package ru.ancientempires;

import ru.ancientempires.helpers.BitmapHelper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import java.util.*;
import java.lang.reflect.*;
import java.sql.*;

public class SomeWithBitmaps
{
	
	public static int ordinal = 0;

	public Bitmap[]	bitmaps;

	public SomeWithBitmaps setBitmaps(Resources res, int[] ids)
	{
		bitmaps = new Bitmap[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			bitmaps[i] = BitmapHelper.getBitmap(res, ids[i]);
		}
		return this;
	}
	
	public Bitmap getBitmap()
	{
		return bitmaps[ordinal % bitmaps.length];
	}
	
}
