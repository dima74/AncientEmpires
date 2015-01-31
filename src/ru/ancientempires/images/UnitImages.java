package ru.ancientempires.images;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipFile;

import ru.ancientempires.activity.MainActivity;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.load.UnitImageProperties;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class UnitImages
{
	
	public static FewBitmaps[][]	unitsBitmaps;
	public static FewBitmaps[][]	greyUnitsBitmaps;
	
	// Используется только во время загрузки игры
	private static String[]			colorPaths;
	private static FewBitmaps[][]	additionalUnitsBitmaps;
	private static int[][]			notColoredI;
	private static int[][]			notColoredJ;
	private static FewBitmaps[][]	baseUnitsBitmaps;
	
	public static FewBitmaps getUnitBitmap(final Unit unit, boolean isLive)
	{
		if (unit.isLive || isLive)
			if (unit.isTurn)
			{
				FewBitmaps[] typeBitmaps = UnitImages.greyUnitsBitmaps[unit.type.ordinal];
				return typeBitmaps[typeBitmaps.length == 1 ? 0 : unit.player.colorI];
			}
			else
				return UnitImages.unitsBitmaps[unit.type.ordinal][unit.player.ordinal];
		else
			return Images.tombstone;
	}
	
	public static void preloadResources(ZipFile images, String path) throws IOException
	{
		JsonReader reader = ZIPHelper.getJsonReader(images, path + "info.json");
		reader.beginObject();
		
		MyAssert.a("color_folders", reader.nextName());
		UnitImages.colorPaths = new String[5]; // красный, зеленый, синий, черный и серый
		reader.beginObject();
		UnitImages.colorPaths[1] = JsonHelper.readString(reader, "red");
		UnitImages.colorPaths[2] = JsonHelper.readString(reader, "green");
		UnitImages.colorPaths[3] = JsonHelper.readString(reader, "blue");
		UnitImages.colorPaths[4] = JsonHelper.readString(reader, "black");
		UnitImages.colorPaths[0] = JsonHelper.readString(reader, "grey");
		reader.endObject();
		
		UnitImages.baseUnitsBitmaps = new FewBitmaps[UnitType.amount][5];
		UnitImages.greyUnitsBitmaps = new FewBitmaps[UnitType.amount][];
		UnitImages.additionalUnitsBitmaps = new FewBitmaps[UnitType.amount][5];
		UnitImages.notColoredI = new int[UnitType.amount][];
		UnitImages.notColoredJ = new int[UnitType.amount][];
		
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		int i = 0;
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			UnitImages.nextUnitImage(reader, images, path);
			i++;
		}
		MyAssert.a(UnitType.amount, i);
		reader.endArray();
		
		reader.endObject();
	}
	
	private static void nextUnitImage(JsonReader reader, ZipFile images, String path) throws IOException
	{
		reader.beginObject();
		int typeI = UnitType.getType(JsonHelper.readString(reader, "type")).ordinal;
		String string = reader.nextName();
		boolean isAdvanced = "isAdvanced".equals(string);
		if (isAdvanced)
		{
			reader.nextBoolean();
			string = reader.nextName();
		}
		MyAssert.a("images", string);
		
		if (!isAdvanced)
		{
			reader.beginArray();
			ArrayList<String> typeBitmaps = new ArrayList<String>();
			while (reader.peek() == JsonToken.STRING)
				typeBitmaps.add(reader.nextString());
			
			// Загрузка серых изображений войнов
			// Bitmap[] greyUnitBitmaps = new Bitmap[typeBitmaps.size()];
			// for (int bitmapI = 0; bitmapI < typeBitmaps.size(); bitmapI++)
			// greyUnitBitmaps[bitmapI] = BitmapHelper.getResizeBitmap(images, path + UnitImages.colorPaths[0] + typeBitmaps.get(bitmapI));
			UnitImages.greyUnitsBitmaps[typeI] = new FewBitmaps[]
			{
					new FewBitmaps().setBitmaps(new Bitmap[]
					{
							BitmapHelper.getResizeBitmap(images, path + UnitImages.colorPaths[0] + typeBitmaps.get(0))
					})
			};
			
			// Загрузка красных, зеленых, синих и черных изображений войнов
			for (int colorI = 1; colorI <= 3; colorI++)
			{
				FewBitmaps someWithBitmaps = new FewBitmaps();
				someWithBitmaps.setAmount(typeBitmaps.size());
				for (int bitmapI = 0; bitmapI < typeBitmaps.size(); bitmapI++)
					someWithBitmaps.setBitmaps(bitmapI, BitmapHelper.getBitmap(images, path + UnitImages.colorPaths[colorI] + typeBitmaps.get(bitmapI)));
				UnitImages.baseUnitsBitmaps[typeI][colorI] = someWithBitmaps;
			}
			reader.endArray();
		}
		else
		{
			UnitImageProperties[] properties = new Gson().fromJson(reader, UnitImageProperties[].class);
			
			// grey
			int length = 1;// properties.length;
			Bitmap[][] greyBitmaps = new Bitmap[5][length];
			for (int bitmapI = 0; bitmapI < length; bitmapI++)
			{
				UnitImageProperties property = properties[bitmapI];
				Bitmap baseGreyBitmap = BitmapHelper.getBitmap(images, path + UnitImages.colorPaths[0] + property.colored);
				greyBitmaps[0][bitmapI] = UnitImages.associateBitmaps(images, baseGreyBitmap, property, path + property.notColoredGrey.other);
				greyBitmaps[1][bitmapI] = UnitImages.associateBitmaps(images, baseGreyBitmap, property, path + property.notColoredGrey.red);
				greyBitmaps[2][bitmapI] = UnitImages.associateBitmaps(images, baseGreyBitmap, property, path + property.notColoredGrey.green);
				greyBitmaps[3][bitmapI] = UnitImages.associateBitmaps(images, baseGreyBitmap, property, path + property.notColoredGrey.blue);
				greyBitmaps[4][bitmapI] = UnitImages.associateBitmaps(images, baseGreyBitmap, property, path + property.notColoredGrey.black);
			}
			UnitImages.greyUnitsBitmaps[typeI] = new FewBitmaps[5];
			for (int colorI = 0; colorI <= 4; colorI++)
				UnitImages.greyUnitsBitmaps[typeI][colorI] = new FewBitmaps().setBitmaps(greyBitmaps[colorI]);
			
			// not colored i, j
			UnitImages.notColoredI[typeI] = new int[properties.length];
			UnitImages.notColoredJ[typeI] = new int[properties.length];
			for (int bitmapI = 0; bitmapI < properties.length; bitmapI++)
			{
				UnitImageProperties property = properties[bitmapI];
				UnitImages.notColoredI[typeI][bitmapI] = property.notColoredI;
				UnitImages.notColoredJ[typeI][bitmapI] = property.notColoredJ;
			}
			
			// base
			Bitmap[][] baseBitmaps = new Bitmap[5][properties.length];
			for (int bitmapI = 0; bitmapI < properties.length; bitmapI++)
			{
				UnitImageProperties property = properties[bitmapI];
				for (int colorI = 0; colorI <= 4; colorI++)
					baseBitmaps[colorI][bitmapI] = BitmapHelper.getBitmap(images, path + UnitImages.colorPaths[colorI] + property.colored);
			}
			for (int colorI = 0; colorI <= 4; colorI++)
				UnitImages.baseUnitsBitmaps[typeI][colorI] = new FewBitmaps().setBitmaps(baseBitmaps[colorI]);
			
			Bitmap[][] additionalBitmaps = UnitImages.getNotColoredBitmaps(images, path, properties);
			for (int colorI = 0; colorI <= 4; colorI++)
				UnitImages.additionalUnitsBitmaps[typeI][colorI] = new FewBitmaps().setBitmaps(additionalBitmaps[colorI]);
		}
		
		reader.endObject();
	}
	
	private static Bitmap associateBitmaps(ZipFile images, Bitmap bitmap, UnitImageProperties property, String other) throws IOException
	{
		Bitmap otherBitmap = BitmapHelper.getBitmap(images, other);
		return BitmapHelper.getResizeBitmap(UnitImages.associateBitmaps(bitmap, property.notColoredI, property.notColoredJ, otherBitmap));
	}
	
	private static Bitmap associateBitmaps(Bitmap bitmap, int startY, int startX, Bitmap otherBitmap)
	{
		Bitmap result = bitmap.copy(Config.ARGB_8888, true);
		for (int otherX = 0; otherX < otherBitmap.getWidth(); otherX++)
			for (int otherY = 0; otherY < otherBitmap.getHeight(); otherY++)
			{
				final int x = startX + otherX;
				final int y = startY + otherY;
				final int otherPixel = otherBitmap.getPixel(otherX, otherY);
				if ((otherPixel & 0xFF000000) != 0)
					result.setPixel(x, y, otherPixel);
			}
		return result;
	}
	
	private static Bitmap[][] getNotColoredBitmaps(ZipFile images, String path, UnitImageProperties[] properties) throws IOException
	{
		Bitmap[][] bitmaps = new Bitmap[5][properties.length];
		for (int i = 0; i < properties.length; i++)
		{
			bitmaps[0][i] = BitmapHelper.getBitmap(images, path + properties[i].notColored.other);
			bitmaps[1][i] = BitmapHelper.getBitmap(images, path + properties[i].notColored.red);
			bitmaps[2][i] = BitmapHelper.getBitmap(images, path + properties[i].notColored.green);
			bitmaps[3][i] = BitmapHelper.getBitmap(images, path + properties[i].notColored.blue);
			bitmaps[4][i] = BitmapHelper.getBitmap(images, path + properties[i].notColored.black);
		}
		return bitmaps;
	}
	
	public static void loadResources(ZipFile images, Game game) throws IOException
	{
		UnitImages.unitsBitmaps = new FewBitmaps[UnitType.amount][game.players.length];
		
		RenderScriptCellImages rs = new RenderScriptCellImages();
		rs.createScript(MainActivity.context);
		for (int typeI = 0; typeI < UnitType.amount; typeI++)
		{
			FewBitmaps r = UnitImages.baseUnitsBitmaps[typeI][1];
			FewBitmaps g = UnitImages.baseUnitsBitmaps[typeI][2];
			FewBitmaps b = UnitImages.baseUnitsBitmaps[typeI][3];
			int bitmapAmount = r.bitmaps.length;
			
			Bitmap[][] bitmaps = new Bitmap[game.players.length][bitmapAmount];
			for (int bitmapI = 0; bitmapI < bitmapAmount; bitmapI++)
			{
				rs.setBitmaps(r.bitmaps[bitmapI], g.bitmaps[bitmapI], b.bitmaps[bitmapI]);
				for (int playerI = 0; playerI < game.players.length; playerI++)
				{
					Bitmap bitmap = rs.getBitmap(game.players[playerI].color);
					if (UnitImages.notColoredI[typeI] != null)
					{
						int colorI = game.players[playerI].colorI;
						bitmap = UnitImages.associateBitmaps(bitmap,
								UnitImages.notColoredI[typeI][bitmapI],
								UnitImages.notColoredJ[typeI][bitmapI],
								UnitImages.additionalUnitsBitmaps[typeI][colorI].bitmaps[bitmapI]);
					}
					bitmaps[playerI][bitmapI] = BitmapHelper.getResizeBitmap(bitmap);
				}
			}
			
			for (int playerI = 0; playerI < game.players.length; playerI++)
				UnitImages.unitsBitmaps[typeI][playerI] = new FewBitmaps().setBitmaps(bitmaps[playerI]);
		}
		
	}
	
}
