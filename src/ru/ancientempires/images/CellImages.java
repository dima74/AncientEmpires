package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import android.graphics.Bitmap;
import ru.ancientempires.MyColor;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import ru.ancientempires.images.bitmaps.CellBitmap;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;

public class CellImages
{
	
	private static CellBitmap[]	cellBitmaps;
	private static CellBitmap[]	cellBitmapsDual;
	
	public static int[]			playerToColorI;
	private static MyColor[]	colors	= new MyColor[]
											{
													MyColor.RED,
													MyColor.GREEN,
													MyColor.BLUE,
													MyColor.BLACK
											};
											
	public static Bitmap getCellBitmap(Cell cell, boolean dual)
	{
		if (dual)
		{
			CellBitmap cellBitmap = CellImages.cellBitmapsDual[cell.type.ordinal];
			return cellBitmap == null ? null : cellBitmap.getBitmap(cell);
		}
		else
			return CellImages.cellBitmaps[cell.type.ordinal].getBitmap(cell);
	}
	
	public static boolean isCellSmokes(Cell cell)
	{
		return cell.isCapture && CellImages.cellBitmaps[cell.type.ordinal].isSmokes;
	}
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		CellImages.cellBitmaps = new CellBitmap[CellType.number];
		CellImages.cellBitmapsDual = new CellBitmap[CellType.number];
		
		JsonReader reader = ZIPHelper.getJsonReader(images, path + "info.json");
		reader.beginObject();
		
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		for (int i = 0; reader.peek() == JsonToken.BEGIN_OBJECT; i++)
		{
			reader.beginObject();
			CellType type = CellType.getType(JsonHelper.readString(reader, "type"));
			MyAssert.a("images", reader.nextName());
			String[] imageNames = new Gson().fromJson(reader, String[].class);
			
			CellBitmap cellBitmap = new CellBitmap();
			
			// defaultBitmap
			Bitmap[] defaultBitmaps = new Bitmap[imageNames.length];
			for (int j = 0; j < defaultBitmaps.length; j++)
				defaultBitmaps[j] = BitmapHelper.getBitmap(images, path + "default/" + imageNames[j]);
			cellBitmap.defaultBitmap = new FewBitmaps().setBitmaps(defaultBitmaps);
			
			// colorBitmaps
			if (type.isCapture)
			{
				cellBitmap.colorBitmaps = new FewBitmaps[CellImages.colors.length];
				for (int colorI = 0; colorI < CellImages.colors.length; colorI++)
				{
					Bitmap[] bitmaps = new Bitmap[imageNames.length];
					for (int j = 0; j < bitmaps.length; j++)
						bitmaps[j] = BitmapHelper.getBitmap(images, path + CellImages.colors[colorI].name + "/" + imageNames[j]);
					cellBitmap.colorBitmaps[colorI] = new FewBitmaps().setBitmaps(bitmaps);
				}
			}
			
			while (reader.peek() == JsonToken.NAME)
			{
				String name = reader.nextName().intern();
				// destroying
				if (name == "imagesDestroying")
				{
					String[] imageNamesDestroying = new Gson().fromJson(reader, String[].class);
					Bitmap[] bitmaps = new Bitmap[imageNamesDestroying.length];
					for (int j = 0; j < bitmaps.length; j++)
						bitmaps[j] = BitmapHelper.getBitmap(images, path + "destroying/" + imageNamesDestroying[j]);
					cellBitmap.destroyingBitmap = new FewBitmaps().setBitmaps(bitmaps);
				}
				
				// dual
				if (name == "isDual")
					cellBitmap.isDual = reader.nextBoolean();
					
				// smoke
				if (name == "isSmokes")
					cellBitmap.isSmokes = reader.nextBoolean();
			}
			(cellBitmap.isDual ? CellImages.cellBitmapsDual : CellImages.cellBitmaps)[type.ordinal] = cellBitmap;
			reader.endObject();
		}
		reader.endArray();
		
		reader.endObject();
	}
	
	public static void loadResources(ZipFile images, String path, Game game) throws IOException
	{
		CellImages.playerToColorI = new int[game.players.length];
		for (Player player : game.players)
			for (int colorI = 0; colorI < CellImages.colors.length; colorI++)
				if (player.color == CellImages.colors[colorI])
					CellImages.playerToColorI[player.ordinal] = colorI;
	}
	
}
