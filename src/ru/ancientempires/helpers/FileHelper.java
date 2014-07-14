package ru.ancientempires.helpers;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.net.Uri;

public class FileHelper
{
	
	public static File getTempFile(Context context, String url)
	{
		File file = null;
		try
		{
			String fileName = Uri.parse(url).getLastPathSegment();
			file = File.createTempFile(fileName, null, context.getExternalCacheDir());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}
	
}
