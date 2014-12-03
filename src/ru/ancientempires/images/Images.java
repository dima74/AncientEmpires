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
	
	public static Bitmap	amountGold;
	public static Bitmap	amountUnits;
	public static Bitmap	arrowStrange;
	public static Bitmap	arrowIncrease;
	public static Bitmap	arrowDecrease;
	public static Bitmap	attack;
	public static Bitmap	defence;
	public static Bitmap	levelIncrease;
	public static Bitmap	levelUp;
	
	public static int		amountGoldH;
	public static int		amountGoldW;
	public static int		amountUnitsH;
	public static int		amountUnitsW;
	
	public static void preloadResources(ZipFile imagesZipFile) throws IOException
	{
		JsonReader reader = new JsonReader(ZIPHelper.getISR(imagesZipFile, "info.json"));
		reader.beginObject();
		String cellsPath = JsonHelper.readString(reader, "cells_folder");
		String unitsPath = JsonHelper.readString(reader, "units_folder");
		String actionsPath = JsonHelper.readString(reader, "actions_folder");
		String numbersPath = JsonHelper.readString(reader, "numbers_folder");
		String bigNumbersPath = JsonHelper.readString(reader, "big_numbers_folder");
		String sparksPath = JsonHelper.readString(reader, "sparks_folder");
		reader.endObject();
		reader.close();
		
		CellImages.preloadResources(imagesZipFile, cellsPath);
		UnitImages.preloadResources(imagesZipFile, unitsPath);
		ActionImages.preloadResources(imagesZipFile, actionsPath);
		NumberImages.preloadResources(imagesZipFile, numbersPath);
		BigNumberImages.preloadResources(imagesZipFile, bigNumbersPath);
		SparksImages.preloadResources(imagesZipFile, sparksPath);
		
		// self
		Images.amountGold = BitmapHelper.getResizeBitmap(imagesZipFile, "amountGold.png");
		Images.amountUnits = BitmapHelper.getResizeBitmap(imagesZipFile, "amountUnits.png");
		Images.arrowStrange = BitmapHelper.getResizeBitmap(imagesZipFile, "arrowStrange.png");
		Images.arrowIncrease = BitmapHelper.getResizeBitmap(imagesZipFile, "arrowIncrease.png");
		Images.arrowDecrease = BitmapHelper.getResizeBitmap(imagesZipFile, "arrowDecrease.png");
		Images.attack = BitmapHelper.getResizeBitmap(imagesZipFile, "attack.png");
		Images.defence = BitmapHelper.getResizeBitmap(imagesZipFile, "defence.png");
		Images.levelIncrease = BitmapHelper.getResizeBitmap(imagesZipFile, "levelIncrease.png");
		Images.levelUp = BitmapHelper.getResizeBitmap(imagesZipFile, "levelUp.png");
		
		Images.amountGoldH = Images.amountGold.getHeight();
		Images.amountGoldW = Images.amountGold.getWidth();
		Images.amountUnitsH = Images.amountUnits.getHeight();
		Images.amountUnitsW = Images.amountUnits.getWidth();
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		CellImages.loadResources(imagesZipFile, game);
		UnitImages.loadResources(imagesZipFile, game);
	}
	
}
