package ru.ancientempires.draws;

import android.graphics.Canvas;

import java.util.Random;

import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.images.ActionImages;
import ru.ancientempires.images.ArrowsImages;
import ru.ancientempires.images.BigNumberImages;
import ru.ancientempires.images.CellImages;
import ru.ancientempires.images.CursorImages;
import ru.ancientempires.images.Images;
import ru.ancientempires.images.SmallNumberImages;
import ru.ancientempires.images.SmokeImages;
import ru.ancientempires.images.SparksImages;
import ru.ancientempires.images.StatusesImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Game;

public class Draw {

	public static final Random random = new Random();

	public static Images Images() {
		return Client.client.images;
	}

	public static CellImages CellImages() {
		return Client.client.images.cell;
	}

	public static UnitImages UnitImages() {
		return Client.client.images.unit;
	}

	public static ActionImages ActionImages() {
		return Client.client.images.action;
	}

	public static SmallNumberImages SmallNumberImages() {
		return Client.client.images.smallNumber;
	}

	public static BigNumberImages BigNumberImages() {
		return Client.client.images.bigNumber;
	}

	public static SparksImages SparksImages() {
		return Client.client.images.sparks;
	}

	public static CursorImages CursorImages() {
		return Client.client.images.cursor;
	}

	public static ArrowsImages ArrowsImages() {
		return Client.client.images.arrows;
	}

	public static StatusesImages StatusesImages() {
		return Client.client.images.statuses;
	}

	public static SmokeImages SmokeImages() {
		return Client.client.images.smoke;
	}

	public static final int   A = 24;
	public static final float a = 1.0f;

	public DrawMain     main;
	public BaseDrawMain mainBase;
	public Game         game;

	public Draw(BaseDrawMain mainBase) {
		this.mainBase = mainBase;
		if (mainBase != null) {
			game = mainBase.game;
			main = mainBase.main;
			MyAssert.a(game != null);
		}
	}

	public BaseGameActivity getActivity() {
		return main.activity;
	}

	public GameActivity getGameActivity() {
		return (GameActivity) main.activity;
	}

	public int h() {
		return mainBase.activity.getView().h;
	}

	public int w() {
		return mainBase.activity.getView().w;
	}

	public int iFrame() {
		return mainBase.iFrame;
	}

	public void postUpdateCampaign() {
		((GameActivity) mainBase.activity).postUpdateCampaign();
	}

	public boolean isEnd() {
		return false;
	}

	public void draw(Canvas canvas) {}
}
