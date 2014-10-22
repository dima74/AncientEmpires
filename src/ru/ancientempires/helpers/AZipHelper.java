package ru.ancientempires.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import ru.ancientempires.MainActivity;

public class AZipHelper
{
	
	public static ZipFile getZipFileFromAssets(String name) throws IOException
	{
		InputStream inputStream = MainActivity.assets.open(name);
		File zipFileOutput = new File(MainActivity.context.getExternalCacheDir(), name);
		FileOutputStream fileOutputStream = new FileOutputStream(zipFileOutput);
		
		// TODO расширить, если файл > 1МБ
		byte[] buffer = new byte[1024 * 1024];
		int length = inputStream.read(buffer);
		fileOutputStream.write(buffer, 0, length);
		
		fileOutputStream.close();
		
		ZipFile zipFile = new ZipFile(new File(MainActivity.context.getExternalCacheDir(), name));
		return zipFile;
	}
	
}
