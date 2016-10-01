package ru.ancientempires.images;

import android.graphics.Bitmap;

import java.io.IOException;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.images.bitmaps.FewBitmaps;
import ru.ancientempires.model.Game;
import ru.ancientempires.rules.Rules;

public class Images extends AbstractImages {

	public static Images get() {
		return Client.client.images;
	}

	// public float baseMulti = 1.0f; // 4.5f / 3.0f;
	public int bitmapSize = 24;
	public Rules rules;

	public void setRules(Rules rules) {
		this.rules = rules;
		cell.rules = rules;
		unit.rules = rules;
	}

	public CellImages        cell        = new CellImages();
	public UnitImages        unit        = new UnitImages();
	public ActionImages      action      = new ActionImages();
	public SmallNumberImages smallNumber = new SmallNumberImages();
	public BigNumberImages   bigNumber   = new BigNumberImages();
	public SparksImages      sparks      = new SparksImages();
	public CursorImages      cursor      = new CursorImages();
	public ArrowsImages      arrows      = new ArrowsImages();
	public StatusesImages    statuses    = new StatusesImages();
	public SmokeImages       smoke       = new SmokeImages();

	public Bitmap     amountGold;
	public Bitmap     amountUnits;
	public Bitmap     attack;
	public Bitmap     defence;
	public Bitmap     levelIncrease;
	public Bitmap     levelUp;
	public FewBitmaps tombstone;
	public Bitmap     gameover;

	public int amountGoldH;
	public int amountGoldW;
	public int amountUnitsH;
	public int amountUnitsW;

	public int levelUpH;
	public int levelUpW;

	@Override
	public void preload(FileLoader loader) throws IOException {
		cell.preload(loader.getLoader("cells"));
		unit.preload(loader.getLoader("units"));
		action.preload(loader.getLoader("actions"));
		smallNumber.preload(loader.getLoader("numbers"));
		bigNumber.preload(loader.getLoader("bigNumbers"));
		sparks.preload(loader.getLoader("sparks"));
		cursor.preload(loader.getLoader("cursors"));
		arrows.preload(loader.getLoader("arrows"));
		statuses.preload(loader.getLoader("statuses"));
		smoke.preload(loader.getLoader("smoke"));

		// self
		amountGold = loader.loadImage("amountGold.png");
		amountUnits = loader.loadImage("amountUnits.png");
		attack = loader.loadImage("attack.png");
		defence = loader.loadImage("defence.png");
		levelIncrease = loader.loadImage("levelIncrease.png");
		levelUp = loader.loadImage("levelUp.png");

		tombstone = new FewBitmaps(loader, "tombstone.png");
		attack = loader.loadImage("gameover.png");

		amountGoldH = amountGold.getHeight();
		amountGoldW = amountGold.getWidth();
		amountUnitsH = amountUnits.getHeight();
		amountUnitsW = amountUnits.getWidth();

		levelUpH = levelUp.getHeight();
		levelUpW = levelUp.getWidth();
	}

	@Override
	public void load(FileLoader loader, Game game) throws IOException {
		cell.load(loader.getLoader("cells"), game);
		unit.load(loader.getLoader("units"), game);
	}

}
