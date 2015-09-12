package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import android.graphics.Bitmap;
import ru.ancientempires.MyColor;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import ru.ancientempires.images.bitmaps.CellBitmap;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;

public class CellImages
{
	
	private static CellBitmap[]	cellBitmaps;
	private static CellBitmap[]	cellBitmapsDual;
	
	// Используется только во время загрузки игры
	private static String[]	colorPaths;
	private static String[]	namesImage;
	private static String[]	namesImageDestroying;
	private static String[]	namesImageDual;
	private static String	defaultImagesFolder;
	private static String	destroyingImagesFolder;
	
	public static Bitmap getCellBitmap(final Cell cell, final boolean dual)
	{
		CellBitmap cellBitmap = CellImages.cellBitmaps[cell.type.ordinal];
		return dual ? cellBitmap.isDual ? CellImages.cellBitmapsDual[cell.type.ordinal].getBitmap(cell)
				: null
				: cellBitmap.getBitmap(cell);
	}
	
	public static boolean isCellSmokes(Cell cell)
	{
		return cell.isCapture && CellImages.cellBitmaps[cell.type.ordinal].isSmokes;
	}
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		JsonReader reader = ZIPHelper.getJsonReader(images, path + "info.json");
		reader.beginObject();
		
		MyAssert.a("color_folders", reader.nextName());
		CellImages.colorPaths = new String[3]; // красный, зеленый, синий
		reader.beginObject();
		CellImages.colorPaths[0] = JsonHelper.readString(reader, "red");
		CellImages.colorPaths[1] = JsonHelper.readString(reader, "green");
		CellImages.colorPaths[2] = JsonHelper.readString(reader, "blue");
		reader.endObject();
		
		CellImages.defaultImagesFolder = JsonHelper.readString(reader, "default_images_folder");
		CellImages.destroyingImagesFolder = JsonHelper.readString(reader, "destroying_images_folder");
		
		CellImages.cellBitmaps = new CellBitmap[CellType.amount];
		CellImages.cellBitmapsDual = new CellBitmap[CellType.amount];
		
		CellImages.namesImage = new String[CellType.amount];
		CellImages.namesImageDestroying = new String[CellType.amount];
		CellImages.namesImageDual = new String[CellType.amount];
		
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		int i = 0;
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			CellImages.nextCellImage(reader, images, path);
			i++;
		}
		MyAssert.a(CellType.amount, i);
		reader.endArray();
		
		reader.endObject();
		reader.close();
	}
	
	private static void nextCellImage(JsonReader reader, ZipFile images, String path) throws IOException
	{
		reader.beginObject();
		CellType type = CellType.getType(JsonHelper.readString(reader, "type"));
		CellImages.namesImage[type.ordinal] = JsonHelper.readString(reader, "image");
		
		CellBitmap cellBitmap = new CellBitmap();
		cellBitmap.defaultBitmap = BitmapHelper.getResizeBitmap(images, path + CellImages.defaultImagesFolder + CellImages.namesImage[type.ordinal]);
		
		while (reader.peek() == JsonToken.NAME)
		{
			String string = reader.nextName();
			if ("imageDual".equals(string))
			{
				CellImages.namesImageDual[type.ordinal] = reader.nextString();
				cellBitmap.isDual = true;
				CellBitmap cellBitmapDual = new CellBitmap();
				cellBitmapDual.defaultBitmap = BitmapHelper.getResizeBitmap(images, path + CellImages.defaultImagesFolder + CellImages.namesImageDual[type.ordinal]);
				CellImages.cellBitmapsDual[type.ordinal] = cellBitmapDual;
			}
			else if ("imageDestroying".equals(string))
			{
				CellImages.namesImageDestroying[type.ordinal] = reader.nextString();
				cellBitmap.destroyingBitmap = BitmapHelper.getResizeBitmap(images, path + CellImages.destroyingImagesFolder + CellImages.namesImageDestroying[type.ordinal]);
			}
			else if ("isSmokes".equals(string))
				cellBitmap.isSmokes = reader.nextBoolean();
		}
		MyAssert.a(!type.isDestroying || cellBitmap.destroyingBitmap != null);
		CellImages.cellBitmaps[type.ordinal] = cellBitmap;
		
		reader.endObject();
	}
	
	public static void loadResources(ZipFile images, String path, Game game) throws IOException
	{
		for (int i = 0; i < CellType.amount; i++)
			if (CellType.getType(i).isCapture)
			{
				CellImages.cellBitmaps[i].colorsBitmaps = CellImages.loadOneColorBitmap(images, game, path, CellImages.namesImage[i]);
				
				if (CellImages.cellBitmaps[i].isDual)
					CellImages.cellBitmapsDual[i].colorsBitmaps = CellImages.loadOneColorBitmap(images, game, path, CellImages.namesImageDual[i]);
			}
	}
	
	private static Bitmap[] loadOneColorBitmap(ZipFile images, Game game, String path, String imageName) throws IOException
	{
		Bitmap[] bitmaps = new Bitmap[game.players.length];
		
		Bitmap bitmapR = BitmapHelper.getBitmap(images, path + CellImages.colorPaths[0] + imageName);
		Bitmap bitmapG = BitmapHelper.getBitmap(images, path + CellImages.colorPaths[1] + imageName);
		Bitmap bitmapB = BitmapHelper.getBitmap(images, path + CellImages.colorPaths[2] + imageName);
		// AssociationScript.rs.setBitmaps(bitmapR, bitmapG, bitmapB);
		
		for (int j = 0; j < game.players.length; j++)
		{
			Bitmap bitmap = null;
			
			MyColor color = game.players[j].color;
			if (color == MyColor.RED)
				bitmap = bitmapR;
			else if (color == MyColor.GREEN)
				bitmap = bitmapG;
			else if (color == MyColor.BLUE)
				bitmap = bitmapB;
			else
				MyAssert.a(false);
			// bitmap = AssociationScript.rs.getBitmap(game.players[j].color);
			bitmap = BitmapHelper.getResizeBitmap(bitmap);
			bitmaps[j] = bitmap;
		}
		return bitmaps;
	}
	
}
