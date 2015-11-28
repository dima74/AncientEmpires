package ru.ancientempires.images;

import java.io.IOException;

import com.google.gson.stream.JsonReader;

import android.graphics.Bitmap;
import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.helpers.ImagesFileHelper;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;

public class Images
{
	
	// public static float baseMulti = 1.0f; // 4.5f / 3.0f;
	public static int bitmapSize = 24;
	
	public static Bitmap		amountGold;
	public static Bitmap		amountUnits;
	public static Bitmap		attack;
	public static Bitmap		defence;
	public static Bitmap		levelIncrease;
	public static Bitmap		levelUp;
	public static FewBitmaps	tombstone;
	public static Bitmap		gameover;
	
	public static int	amountGoldH;
	public static int	amountGoldW;
	public static int	amountUnitsH;
	public static int	amountUnitsW;
	
	public static int	levelUpH;
	public static int	levelUpW;
	
	public static void preloadResources() throws IOException
	{
		JsonReader reader = ImagesFileHelper.getReader("info.json");
		reader.beginObject();
		String cellsPath = JsonHelper.readString(reader, "cells_folder");
		String unitsPath = JsonHelper.readString(reader, "units_folder");
		String actionsPath = JsonHelper.readString(reader, "actions_folder");
		String numbersPath = JsonHelper.readString(reader, "numbers_folder");
		String bigNumbersPath = JsonHelper.readString(reader, "big_numbers_folder");
		String sparksPath = JsonHelper.readString(reader, "sparks_folder");
		String cursorsPath = JsonHelper.readString(reader, "cursors_folder");
		String arrowsPath = JsonHelper.readString(reader, "arrows_folder");
		String statusesPath = JsonHelper.readString(reader, "statuses_folder");
		String smokePath = JsonHelper.readString(reader, "smoke_folder");
		reader.endObject();
		reader.close();
		
		CellImages.preload(cellsPath);
		UnitImages.preload(unitsPath);
		ActionImages.preload(actionsPath);
		SmallNumberImages.preload(numbersPath);
		BigNumberImages.preload(bigNumbersPath);
		SparksImages.preload(sparksPath);
		CursorImages.preload(cursorsPath);
		ArrowsImages.preload(arrowsPath);
		StatusesImages.preload(statusesPath);
		SmokeImages.preload(smokePath);
		
		// self
		Images.amountGold = BitmapHelper.getResizeBitmap("amountGold.png");
		Images.amountUnits = BitmapHelper.getResizeBitmap("amountUnits.png");
		Images.attack = BitmapHelper.getResizeBitmap("attack.png");
		Images.defence = BitmapHelper.getResizeBitmap("defence.png");
		Images.levelIncrease = BitmapHelper.getResizeBitmap("levelIncrease.png");
		Images.levelUp = BitmapHelper.getResizeBitmap("levelUp.png");
		
		Images.tombstone = new FewBitmaps().setBitmaps("", "tombstone.png");
		Images.attack = BitmapHelper.getBitmap("gameover.png");
		
		Images.amountGoldH = Images.amountGold.getHeight();
		Images.amountGoldW = Images.amountGold.getWidth();
		Images.amountUnitsH = Images.amountUnits.getHeight();
		Images.amountUnitsW = Images.amountUnits.getWidth();
		
		Images.levelUpH = Images.levelUp.getHeight();
		Images.levelUpW = Images.levelUp.getWidth();
		
		Images.bitmapSize = Images.tombstone.bitmaps[0].getHeight();
	}
	
	public static void loadResources(Game game) throws IOException
	{
		JsonReader reader = ImagesFileHelper.getReader("info.json");
		reader.beginObject();
		String cellsPath = JsonHelper.readString(reader, "cells_folder");
		reader.close();
		
		CellImages.loadResources(cellsPath, game);
		UnitImages.loadResources(game);
	}
	
}
