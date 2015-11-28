package ru.ancientempires.helpers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import ru.ancientempires.activity.MainActivity;
import ru.ancientempires.framework.MyAssert;

public class AssetsHelperAndroid extends AssetsHelper
{
	
	private AssetManager assets;
	
	public AssetsHelperAndroid(AssetManager assets)
	{
		this.assets = assets;
	}
	
	@Override
	public InputStream openIS(String name) throws IOException
	{
		return assets.open(name);
	}
	
	public static void copyFilesWithPrefix(String prefix) throws IOException
	{
		for (String file : MainActivity.assets.list(""))
			if (file.startsWith(prefix))
				AssetsHelperAndroid.copyFile(file);
	}
	
	private static byte[] buffer = new byte[1024 * 1024];
	
	public static void copyFile(String file) throws IOException
	{
		InputStream inputStream = MainActivity.assets.open(file);
		FileOutputStream outputStream = MainActivity.context.openFileOutput(file, Context.MODE_PRIVATE);
		
		// TODO расширить, если файл > 1МБ
		MyAssert.a(inputStream.available() < AssetsHelperAndroid.buffer.length);
		int length = inputStream.read(AssetsHelperAndroid.buffer);
		outputStream.write(AssetsHelperAndroid.buffer, 0, length);
		
		inputStream.close();
		outputStream.close();
	}
	
}
