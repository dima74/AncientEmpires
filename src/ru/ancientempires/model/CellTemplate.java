package ru.ancientempires.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.ImagesLoader;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.rules.RulesLoader;
import ru.ancientempires.rules.RulesSaver;

public class CellTemplate
{
	
	public static CellTemplate fromJSON(JsonElement element, RulesLoader loader, CellType cellType)
	{
		JsonObject object = element.getAsJsonObject();
		CellTemplateType type = CellTemplateType.valueOf(object.get("type").getAsString());
		CellType[] friends = loader.getCellTypes(object.get("friends"));
		CellTemplate template = new CellTemplate(type, cellType, friends);
		return template;
	}
	
	public JsonElement toJSON(RulesSaver saver)
	{
		JsonObject object = new JsonObject();
		object.addProperty("type", type.name());
		object.add("friends", saver.toArray(friends.toArray(new CellType[0])));
		return object;
	}
	
	public FewBitmaps[]			bitmaps	= new FewBitmaps[256];
	public CellTemplateType		type;
								
	public CellType				cellType;
	public HashSet<CellType>	friends	= new HashSet<>();
										
	public CellTemplate(CellTemplateType type, CellType cellType, CellType... friendTypes)
	{
		this.type = type;
		this.cellType = cellType;
		friends.addAll(Arrays.asList(friendTypes));
	}
	
	public FewBitmaps get(Cell cell)
	{
		return bitmaps[cell.specialization];
	}
	
	public final void load(ImagesLoader loader, CellType type) throws IOException
	{
		loader = loader.getLoader(type.name).getImagesLoader();
		String[] images = loader.list("");
		for (String image : images)
		{
			int i = Integer.valueOf(image.substring(0, image.indexOf('.')));
			bitmaps[i] = new FewBitmaps(loader.loadImage(image));
		}
		
		for (int i = 0; i < 256; i++)
			if (bitmaps[i] == null)
			{
				// 012
				// 3*4
				// 567
				
				// 1 2 4
				// 8 * 16
				// 32 64 128
				
				Bitmap[][] parts = new Bitmap[2][2];
				parts[0][0] = get(i, 0, 0, 1, 0, 3);
				parts[0][1] = get(i, 0, 1, 1, 2, 4);
				parts[1][0] = get(i, 1, 0, 3, 5, 6);
				parts[1][1] = get(i, 1, 1, 4, 7, 6);
				
				Bitmap bitmap = Bitmap.createBitmap(24, 24, Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				for (int h = 0; h < 2; h++)
					for (int w = 0; w < 2; w++)
						canvas.drawBitmap(parts[h][w], w * 12, h * 12, null);
				bitmaps[i] = new FewBitmaps(bitmap);
			}
	}
	
	private Bitmap get(int i, int h, int w, int i0, int i1, int i2)
	{
		int m = 1 << i0 | 1 << i2;
		if ((i & (1 << i0 | 1 << i2)) == 0)
			m |= 1 << i1;
		for (int j = 0; j < 256; j++)
			if (bitmaps[j] != null && (i & m) == (j & m))
				return Bitmap.createBitmap(bitmaps[j].getBitmap(), w * 12, h * 12, 12, 12);
		MyAssert.a(false);
		return null;
	}
	
	public void update(Cell cell)
	{
		int mask = 255;
		int i = 0;
		for (int di = -1; di <= 1; di++)
			for (int dj = -1; dj <= 1; dj++)
				if (di != 0 || dj != 0)
				{
					if (isFriend(cell, cell.i + di, cell.j + dj))
						mask -= 1 << i;
					i++;
				}
		cell.specialization = mask;
	}
	
	private boolean isFriend(Cell cell, int i, int j)
	{
		if (!cell.game.checkCoordinates(i, j))
			return true;
		Cell target = cell.game.fieldCells[i][j];
		return cellType == target.type || friends.contains(target.type);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", type, friends);
	}
	
}
