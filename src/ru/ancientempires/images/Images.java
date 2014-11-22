package ru.ancientempires.images;

import java.io.IOException;
import java.util.zip.ZipFile;

import ru.ancientempires.helpers.BitmapHelper;
import ru.ancientempires.model.Game;
import android.graphics.Bitmap;

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
		CellImages.preloadResources(imagesZipFile);
		UnitImages.preloadResources(imagesZipFile);
		ActionImages.preloadResources(imagesZipFile);
		NumberImages.preloadResources(imagesZipFile);
		BigNumberImages.preloadResources(imagesZipFile);
		Images.preloadSelfResources(imagesZipFile);
	}
	
	public static void loadResources(ZipFile imagesZipFile, Game game) throws IOException
	{
		CellImages.loadResources(imagesZipFile, game);
		UnitImages.loadResources(imagesZipFile, game);
	}
	
	private static void preloadSelfResources(ZipFile imagesZipFile) throws IOException
	{
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
	
}
