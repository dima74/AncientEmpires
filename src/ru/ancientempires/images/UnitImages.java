package ru.ancientempires.images;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import android.graphics.Bitmap;
import ru.ancientempires.MyColor;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.rules.Rules;

public class UnitImages extends IImages
{
	
	public static UnitImages get()
	{
		return Client.client.images.unit;
	}
	
	public Rules rules;
	
	public MyColor[]		colors	= MyColor.values();
	public int[]			playerToColorI;
	public FewBitmaps[][]	greyUnitsBitmaps;
	public FewBitmaps[][]	unitsBitmaps;
	public Bitmap[][]		unitsBitmapsBuy;
	
	public FewBitmaps getUnitBitmap(Unit unit, boolean keepTurn)
	{
		if (unit.isTurn && !keepTurn)
			return unitsBitmaps[unit.type.ordinal][0];
		else
			return unitsBitmaps[unit.type.ordinal][playerToColorI[unit.player.ordinal]];
	}
	
	public Bitmap getUnitBitmapBuy(Unit unit)
	{
		return unitsBitmapsBuy[unit.type.ordinal][playerToColorI[unit.player.ordinal]];
	}
	
	@Override
	public void preload(ImagesLoader loader) throws IOException
	{
		JsonReader reader = loader.getReader("info.json");
		reader.beginObject();
		
		unitsBitmaps = new FewBitmaps[rules.unitTypes.length][5];
		
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			reader.beginObject();
			int type = rules.getUnitType(JsonHelper.readString(reader, "type")).ordinal;
			MyAssert.a("images", reader.nextName());
			String[] imageNames = new Gson().fromJson(reader, String[].class);
			for (int colorI = 0; colorI < colors.length; colorI++)
			{
				Bitmap[] bitmaps = new Bitmap[imageNames.length];
				for (int j = 0; j < bitmaps.length; j++)
					bitmaps[j] = loader.loadImage(colors[colorI].folderName() + "/" + imageNames[j]);
				unitsBitmaps[type][colorI] = new FewBitmaps().setBitmaps(bitmaps);
			}
			reader.endObject();
		}
		
		reader.endArray();
		
		reader.endObject();
		reader.close();
	}
	
	@Override
	public void load(ImagesLoader loader, Game game) throws IOException
	{
		playerToColorI = new int[game.players.length];
		for (Player player : game.players)
			for (int colorI = 0; colorI < colors.length; colorI++)
				if (player.color == colors[colorI])
					playerToColorI[player.ordinal] = colorI;
					
		unitsBitmapsBuy = new Bitmap[rules.unitTypes.length][5];
		for (int colorI = 0; colorI < colors.length; colorI++)
			for (int typeI = 0; typeI < unitsBitmapsBuy.length; typeI++)
				unitsBitmapsBuy[typeI][colorI] = Bitmap.createScaledBitmap(unitsBitmaps[typeI][colorI].bitmaps[0], 48, 48, false);
	}
	
}
