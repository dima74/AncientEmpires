package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.helpers.ZIPHelper;
import ru.ancientempires.model.Game;
import android.graphics.Bitmap;

import com.google.gson.stream.JsonReader;

public class Images
{
	
	public static float				baseMulti	= 4.5f / 3.0f;
	public static int				bitmapSize;
	
	public static Bitmap			amountGold;
	public static Bitmap			amountUnits;
	public static Bitmap			arrowStrange;
	public static Bitmap			arrowIncrease;
	public static Bitmap			arrowDecrease;
	public static Bitmap			attack;
	public static Bitmap			defence;
	public static Bitmap			levelIncrease;
	public static Bitmap			levelUp;
	public static SomeWithBitmaps	tombstone;
	
	public static int				amountGoldH;
	public static int				amountGoldW;
	public static int				amountUnitsH;
	public static int				amountUnitsW;
	
	public static void preloadResources(ZipFile images) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(images, "info.json"));
		reader.beginObject();
		String cellsPath = JsonHelper.readString(reader, "cells_folder");
		String unitsPath = JsonHelper.readString(reader, "units_folder");
		String actionsPath = JsonHelper.readString(reader, "actions_folder");
		String numbersPath = JsonHelper.readString(reader, "numbers_folder");
		String bigNumbersPath = JsonHelper.readString(reader, "big_numbers_folder");
		String sparksPath = JsonHelper.readString(reader, "sparks_folder");
		String cursorsPath = JsonHelper.readString(reader, "cursors_folder");
		reader.endObject();
		reader.close();
		
		CellImages.preloadResources(images, cellsPath);
		UnitImages.preloadResources(images, unitsPath);
		ActionImages.preloadResources(images, actionsPath);
		NumberImages.preloadResources(images, numbersPath);
		BigNumberImages.preloadResources(images, bigNumbersPath);
		SparksImages.preloadResources(images, sparksPath);
		CursorImages.preloadResources(images, cursorsPath);
		
		// self
		Images.amountGold = BitmapHelper.getResizeBitmap(images, "amountGold.png");
		Images.amountUnits = BitmapHelper.getResizeBitmap(images, "amountUnits.png");
		Images.arrowStrange = BitmapHelper.getResizeBitmap(images, "arrowStrange.png");
		Images.arrowIncrease = BitmapHelper.getResizeBitmap(images, "arrowIncrease.png");
		Images.arrowDecrease = BitmapHelper.getResizeBitmap(images, "arrowDecrease.png");
		Images.attack = BitmapHelper.getResizeBitmap(images, "attack.png");
		Images.defence = BitmapHelper.getResizeBitmap(images, "defence.png");
		Images.levelIncrease = BitmapHelper.getResizeBitmap(images, "levelIncrease.png");
		Images.levelUp = BitmapHelper.getResizeBitmap(images, "levelUp.png");
		
		Images.tombstone = new SomeWithBitmaps().setBitmaps("", "tombstone.png");
		
		Images.amountGoldH = Images.amountGold.getHeight();
		Images.amountGoldW = Images.amountGold.getWidth();
		Images.amountUnitsH = Images.amountUnits.getHeight();
		Images.amountUnitsW = Images.amountUnits.getWidth();
		
		Images.bitmapSize = Images.tombstone.bitmaps[0].getHeight();
	}
	
	public static void loadResources(ZipFile images, Game game) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(images, "info.json"));
		reader.beginObject();
		String cellsPath = JsonHelper.readString(reader, "cells_folder");
		reader.close();
		
		CellImages.loadResources(images, cellsPath, game);
		UnitImages.loadResources(images, game);
	}
	
}
