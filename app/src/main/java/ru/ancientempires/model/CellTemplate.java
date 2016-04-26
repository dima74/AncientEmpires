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
		template.setFriendsUp(loader.getCellTypes(object.get("friendsUp")));
		return template;
	}
	
	public JsonElement toJSON(RulesSaver saver)
	{
		JsonObject object = new JsonObject();
		object.addProperty("type", type.name());
		object.add("friends", saver.toArray(friends.toArray(new CellType[0])));
		object.add("friendsUp", saver.toArray(friendsUp.toArray(new CellType[0])));
		return object;
	}
	
	public FewBitmaps[]			bitmaps		= new FewBitmaps[256];
	public CellTemplateType		type;
								
	public CellType				cellType;
	public HashSet<CellType>	friends		= new HashSet<>();
	public HashSet<CellType>	friendsUp	= new HashSet<>();
											
	public CellTemplate(CellTemplateType type, CellType cellType, CellType... friendTypes)
	{
		this.type = type;
		this.cellType = cellType;
		friends.addAll(Arrays.asList(friendTypes));
	}
	
	public CellTemplate setFriendsUp(CellType... friendTypes)
	{
		friendsUp.addAll(Arrays.asList(friendTypes));
		return this;
	}
	
	public FewBitmaps get(Cell cell)
	{
		return bitmaps[cell.specialization];
	}
	
	public final void load(ImagesLoader loader, CellType cellType) throws IOException
	{
		loader = loader.getLoader(cellType.name).getImagesLoader();
		String[] images = loader.list("");
		for (String image : images)
		{
			int i = Integer.valueOf(image.substring(0, image.indexOf('.')));
			bitmaps[i] = new FewBitmaps(loader.loadImage(image));
		}
		
		for (int i = 0; i < bitmaps.length; i++)
			if (bitmaps[i] == null)
			{
				// if (type == CellTemplateType.WATER)
				{
					int equalI = i;
					if ((i & 2) > 0)
						equalI &= ~5;
					if ((i & 8) > 0)
						equalI &= ~33;
					if ((i & 16) > 0)
						equalI &= ~132;
					if ((i & 64) > 0)
						equalI &= ~160;
					if (equalI < i)
					{
						bitmaps[i] = bitmaps[equalI];
						continue;
					}
				}
				
				// 012
				// 3*4
				// 567
				
				// 1 2 4
				// 8 * 16
				// 32 64 128
				
				Bitmap[][] parts = new Bitmap[2][2];
				// if (type == CellTemplateType.WATER)
				{
					parts[0][0] = getWater(i, 0, 0, 1, 0, 3);
					parts[0][1] = getWater(i, 0, 1, 1, 2, 4);
					parts[1][0] = getWater(i, 1, 0, 3, 5, 6);
					parts[1][1] = getWater(i, 1, 1, 4, 7, 6);
				}
				/*
				else
				{
					// +0+
					// 1*2
					// +3+
					
					// +1+
					// 2*4
					// +8+
					
					parts[0][0] = getWay(i, 0, 0, 0, 1);
					parts[0][1] = getWay(i, 0, 1, 0, 2);
					parts[1][0] = getWay(i, 1, 0, 1, 3);
					parts[1][1] = getWay(i, 1, 1, 2, 3);
				}
				//*/
				
				Bitmap bitmap = Bitmap.createBitmap(24, 24, Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				for (int h = 0; h < 2; h++)
					for (int w = 0; w < 2; w++)
						canvas.drawBitmap(parts[h][w], w * 12, h * 12, null);
				bitmaps[i] = new FewBitmaps(bitmap);
			}
	}
	
	private Bitmap getWay(int i, int h, int w, int i0, int i1)
	{
		int m = 1 << i0 | 1 << i1;
		for (int j = 0; j < bitmaps.length; j++)
			if (bitmaps[j] != null && (i & m) == (j & m))
				return Bitmap.createBitmap(bitmaps[j].getBitmap(), w * 12, h * 12, 12, 12);
		MyAssert.a(false);
		return null;
	}
	
	private Bitmap getWater(int i, int h, int w, int i0, int i1, int i2)
	{
		int m = 1 << i0 | 1 << i2;
		if ((i & (1 << i0 | 1 << i2)) == 0)
			m |= 1 << i1;
		for (int j = 0; j < bitmaps.length; j++)
			if (bitmaps[j] != null && (i & m) == (j & m))
				return Bitmap.createBitmap(bitmaps[j].getBitmap(), w * 12, h * 12, 12, 12);
		MyAssert.a(false);
		return null;
	}
	
	public void update(Cell cell)
	{
		int mask = bitmaps.length - 1;
		int i = 0;
		for (int di = -1; di <= 1; di++)
			for (int dj = -1; dj <= 1; dj++)
				if (di != 0 || dj != 0)// && (type == CellTemplateType.WATER || di == 0 || dj == 0))
				{
					if (isFriend(cell, cell.i + di, cell.j + dj))
						mask -= 1 << i;
					i++;
				}
		if (cell.i > 0 && friendsUp.contains(cell.game.fieldCells[cell.i - 1][cell.j].type))
			mask |= 5;
		cell.specialization = mask;
	}
	
	private boolean isFriend(Cell cell, int i, int j)
	{
		if (!cell.game.checkCoordinates(i, j))
			return true;
		Cell target = cell.game.fieldCells[i][j];
		return cellType == target.type || friends.contains(target.type)
				|| j == cell.j && i + 1 == cell.i && friendsUp.contains(target.type);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s %s", type, friends);
	}
	
}
