package ru.ancientempires.images;

import android.graphics.Bitmap;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;

import ru.ancientempires.MyColor;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.rules.Rules;

public class UnitImages extends AbstractImages {

	public static UnitImages get() {
		return Client.client.images.unit;
	}

	public Rules rules;

	public MyColor[] colors = MyColor.values();
	public int[]          playerToColorI;
	public FewBitmaps[][] greyUnitBitmaps;
	public FewBitmaps[][] unitBitmaps;
	public Bitmap[][]     unitBitmapsBuy;

	public FewBitmaps getUnitBitmap(Unit unit, boolean keepTurn) {
		if (unit.isTurn && !keepTurn)
			return unitBitmaps[unit.type.ordinal][0];
		else
			return unitBitmaps[unit.type.ordinal][playerToColorI[unit.player.ordinal]];
	}

	public boolean containsBitmap(UnitType type) {
		return unitBitmaps[type.ordinal][0] != null;
	}

	public Bitmap getUnitBitmapBuy(Unit unit) {
		return unitBitmapsBuy[unit.type.ordinal][playerToColorI[unit.player.ordinal]];
	}

	@Override
	public void preload(FileLoader loader) throws IOException {
		JsonReader reader = loader.getReader("info.json");
		reader.beginObject();

		unitBitmaps = new FewBitmaps[rules.unitTypes.length][5];

		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT) {
			reader.beginObject();
			int type = rules.getUnitType(JsonHelper.readString(reader, "type")).ordinal;
			MyAssert.a("images", reader.nextName());
			String[] imageNames = new Gson().fromJson(reader, String[].class);
			for (int colorI = 0; colorI < colors.length; colorI++) {
				Bitmap[] bitmaps = new Bitmap[imageNames.length];
				for (int j = 0; j < bitmaps.length; j++)
					bitmaps[j] = loader.loadImage(colors[colorI].folderName() + "/" + imageNames[j]);
				unitBitmaps[type][colorI] = new FewBitmaps(bitmaps);
			}
			reader.endObject();
		}
		reader.endArray();

		reader.endObject();
		reader.close();
	}

	@Override
	public void load(FileLoader loader, Game game) throws IOException {
		playerToColorI = new int[game.players.length];
		for (Player player : game.players)
			for (int colorI = 0; colorI < colors.length; colorI++)
				if (player.color == colors[colorI])
					playerToColorI[player.ordinal] = colorI;

		unitBitmapsBuy = new Bitmap[rules.unitTypes.length][5];
		for (int colorI = 0; colorI < colors.length; colorI++)
			for (int typeI = 1; typeI < unitBitmapsBuy.length; typeI++)
				unitBitmapsBuy[typeI][colorI] = Bitmap.createScaledBitmap(unitBitmaps[typeI][colorI].bitmaps[0], 48, 48, false);
	}
}
