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
import ru.ancientempires.images.bitmaps.CellBitmap;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.rules.Rules;

public class CellImages extends AbstractImages {

	public static CellImages get() {
		return Client.client.images.cell;
	}

	public Rules rules;

	public CellBitmap[] cellBitmaps;
	public CellBitmap[] cellBitmapsDual;

	public int[] playerToColorI;
	public MyColor[] colors = new MyColor[]
			{
					MyColor.RED,
					MyColor.GREEN,
					MyColor.BLUE,
					MyColor.BLACK
			};

	public FewBitmaps getCellBitmap(Cell cell, boolean dual) {
		if (cell.type.template != null)
			return dual ? null : cell.type.template.get(cell);
		if (dual) {
			CellBitmap cellBitmap = cellBitmapsDual[cell.type.ordinal];
			return cellBitmap == null ? null : cellBitmap.getBitmap(cell);
		} else
			return cellBitmaps[cell.type.ordinal].getBitmap(cell);
	}

	public boolean containsBitmap(CellType type) {
		return type.template != null || cellBitmaps[type.ordinal] != null;
	}

	public boolean isCellSmokes(Cell cell) {
		return cell.isCapture() && cellBitmaps[cell.type.ordinal].isSmokes;
	}

	@Override
	public void preload(FileLoader loader) throws IOException {
		cellBitmaps = new CellBitmap[rules.cellTypes.length];
		cellBitmapsDual = new CellBitmap[rules.cellTypes.length];

		JsonReader reader = loader.getReader("info.json");
		reader.beginObject();
		MyAssert.a("images", reader.nextName());
		reader.beginArray();
		for (int i = 0; reader.peek() == JsonToken.BEGIN_OBJECT; i++) {
			reader.beginObject();
			CellType type = rules.getCellType(JsonHelper.readString(reader, "type"));
			MyAssert.a("images", reader.nextName());
			String[] imageNames = new Gson().fromJson(reader, String[].class);

			CellBitmap cellBitmap = new CellBitmap();

			// defaultBitmap
			Bitmap[] defaultBitmaps = new Bitmap[imageNames.length];
			for (int j = 0; j < defaultBitmaps.length; j++)
				defaultBitmaps[j] = loader.loadImage("default/" + imageNames[j]);
			cellBitmap.defaultBitmap = new FewBitmaps(defaultBitmaps);

			// colorBitmaps
			if (type.isCapturing) {
				cellBitmap.colorBitmaps = new FewBitmaps[colors.length];
				for (int colorI = 0; colorI < colors.length; colorI++) {
					Bitmap[] bitmaps = new Bitmap[imageNames.length];
					for (int j = 0; j < bitmaps.length; j++)
						bitmaps[j] = loader.loadImage(colors[colorI].folderName() + "/" + imageNames[j]);
					cellBitmap.colorBitmaps[colorI] = new FewBitmaps(bitmaps);
				}
			}

			while (reader.peek() == JsonToken.NAME) {
				String name = reader.nextName().intern();
				// dual
				if (name == "isDual")
					cellBitmap.isDual = reader.nextBoolean();

				// smoke
				if (name == "isSmokes")
					cellBitmap.isSmokes = reader.nextBoolean();
			}
			(cellBitmap.isDual ? cellBitmapsDual : cellBitmaps)[type.ordinal] = cellBitmap;
			reader.endObject();
		}
		reader.endArray();
		reader.endObject();

		for (CellType type : rules.cellTypes)
			if (type.template != null)
				type.template.load(loader, type);
	}

	@Override
	public void load(FileLoader loader, Game game) throws IOException {
		playerToColorI = new int[game.players.length];
		for (Player player : game.players)
			for (int colorI = 0; colorI < colors.length; colorI++)
				if (player.color == colors[colorI])
					playerToColorI[player.ordinal] = colorI;
		for (Cell[] line : game.fieldCells)
			for (Cell cell : line)
				if (cell.type.template != null)
					cell.type.template.update(cell);
	}

}
