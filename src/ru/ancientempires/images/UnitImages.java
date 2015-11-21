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
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class UnitImages
{
	
	private static MyColor[]		colors	= MyColor.colors;
	public static int[]				playerToColorI;
	public static FewBitmaps[][]	greyUnitsBitmaps;
	private static FewBitmaps[][]	unitsBitmaps;
	private static Bitmap[][]		unitsBitmapsBuy;
	
	public static FewBitmaps getUnitBitmap(Unit unit, boolean keepTurn)
	{
		if (unit.isTurn && !keepTurn)
			return UnitImages.unitsBitmaps[unit.type.ordinal][0];
		else
			return UnitImages.unitsBitmaps[unit.type.ordinal][UnitImages.playerToColorI[unit.player.ordinal]];
	}
	
	public static Bitmap getUnitBitmapBuy(Unit unit)
	{
		return UnitImages.unitsBitmapsBuy[unit.type.ordinal][UnitImages.playerToColorI[unit.player.ordinal]];
	}
	
	public static void preload(ZipFile images, String path) throws IOException
	{
		JsonReader reader = ZIPHelper.getJsonReader(images, path + "info.json");
		reader.beginObject();
		
		UnitImages.unitsBitmaps = new FewBitmaps[UnitType.number][5];
		
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			reader.beginObject();
			int type = UnitType.getType(JsonHelper.readString(reader, "type")).ordinal;
			MyAssert.a("images", reader.nextName());
			String[] imageNames = new Gson().fromJson(reader, String[].class);
			for (int colorI = 0; colorI < UnitImages.colors.length; colorI++)
			{
				Bitmap[] bitmaps = new Bitmap[imageNames.length];
				for (int j = 0; j < bitmaps.length; j++)
					bitmaps[j] = BitmapHelper.getBitmap(images, path + UnitImages.colors[colorI].name + "/" + imageNames[j]);
				UnitImages.unitsBitmaps[type][colorI] = new FewBitmaps().setBitmaps(bitmaps);
			}
			reader.endObject();
		}
		
		reader.endArray();
		
		reader.endObject();
		reader.close();
	}
	
	public static void loadResources(ZipFile images, Game game) throws IOException
	{
		UnitImages.playerToColorI = new int[game.players.length];
		for (Player player : game.players)
			for (int colorI = 0; colorI < UnitImages.colors.length; colorI++)
				if (player.color == UnitImages.colors[colorI])
					UnitImages.playerToColorI[player.ordinal] = colorI;
					
		UnitImages.unitsBitmapsBuy = new Bitmap[UnitType.number][5];
		for (int colorI = 0; colorI < UnitImages.colors.length; colorI++)
			for (int typeI = 0; typeI < UnitType.number; typeI++)
				UnitImages.unitsBitmapsBuy[typeI][colorI] = Bitmap.createScaledBitmap(UnitImages.unitsBitmaps[typeI][colorI].bitmaps[0], 48, 48, false);
	}
	
}
