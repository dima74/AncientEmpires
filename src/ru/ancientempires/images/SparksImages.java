package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import android.graphics.Bitmap;

import com.google.gson.stream.JsonReader;

public class SparksImages
{
	
	private static Bitmap[]	attackBitmaps;
	public static int		amountAttack;
	
	public static int		h;
	public static int		w;
	
	public static Bitmap getAttackBitmap(int number)
	{
		return SparksImages.attackBitmaps[number % SparksImages.amountAttack];
	}
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(images, path + "info.json"));
		reader.beginObject();
		String pathAttack = path + JsonHelper.readString(reader, "attack_spark_folder");
		reader.endObject();
		reader.close();
		SparksImages.preloadResourcesAtttack(images, pathAttack);
	}
	
	private static void preloadResourcesAtttack(ZipFile images, String path) throws IOException
	{
		SparksImages.amountAttack = 6;
		SparksImages.attackBitmaps = new Bitmap[SparksImages.amountAttack];
		for (int i = 0; i < SparksImages.amountAttack; i++)
			SparksImages.attackBitmaps[i] = BitmapHelper.getResizeBitmap(images, path + i + ".png");
		SparksImages.h = SparksImages.attackBitmaps[0].getHeight();
		SparksImages.w = SparksImages.attackBitmaps[0].getWidth();
	}
	
}
