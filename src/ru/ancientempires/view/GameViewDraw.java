package ru.ancientempires.view;

import java.io.IOException;
import java.util.Random;

import ru.ancientempires.images.Images;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

public class GameViewDraw
{
	
	public static boolean	isBaseCellSizeDetermine	= false;
	
	public static int		baseH;
	public static int		baseW;
	public static int		A;
	public static int		a;										// 1/12 A
																	
	public static float		baseMulti				= 4.5f / 3.0f;
	
	public static void initResources(Resources res) throws IOException
	{
		// GameDrawAction.initResources();
	}
	
	// не знаю, как выводится эта константа
	public static final int		DELAY_BETWEEN_UPDATES	= 265;
	
	public int					offsetY					= 0;
	public int					offsetX					= 0;
	
	// Отображение игры
	private GameViewPart		gameViewCell;
	private ZoneView			zoneViewWay;
	private ZoneView			zoneViewAttack;
	private GameViewPart		gameViewCellDual;
	private GameViewUnit		gameViewUnit;
	private GameViewPart		gameViewRaise;
	private GameViewPart		gameViewAction;
	private GameViewInfo		gameViewInfo;
	public GameViewCursor		gameViewCursor;
	private AnimateAttackView	animateAttackView;
	
	public GameViewDraw(Context context)
	{
		/*
		this.gameViewCell = new GameViewCell(context, this)
				.setDual(false).setField(this.game.map.getField());
		this.zoneViewWay = new ZoneView(context, this);
		this.zoneViewAttack = new ZoneView(context, this);
		this.gameViewCellDual = new GameViewCell(context, this)
				.setDual(true).setField(this.game.map.getField());
		
		this.gameViewUnit = new GameViewUnit(context, this)
				.setField(this.game.fieldUnits)
				.setWayView(this.zoneViewWay)
				.setAttackView(this.zoneViewAttack);
		this.gameViewCursor = new GameViewCursor(context, this);
		this.gameViewAction = new GameViewAction(context, this);
		this.gameViewInfo = new GameViewInfo(context, this).setField(this.game.map.getField())
				.updatePlayer(this.game.currentPlayer);
		*/
		this.animateAttackView = new AnimateAttackView(context);
		this.gameViewUnit
				.setGameViewCursor(this.gameViewCursor)
				.setAnimateAttackView(this.animateAttackView);
		
		this.gameViewCell.setLayoutParams(OldGameView.fullLayoutParams);
		this.gameViewUnit.setLayoutParams(OldGameView.fullLayoutParams);
		this.gameViewCellDual.setLayoutParams(OldGameView.fullLayoutParams);
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(Images.tombstone.getBitmap(), new Random().nextInt() % 100 + 100, 200, null);
	}
	
}
