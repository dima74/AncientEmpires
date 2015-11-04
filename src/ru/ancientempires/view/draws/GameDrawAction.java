package ru.ancientempires.view.draws;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.ActionImages;
import ru.ancientempires.view.GameView;
import ru.ancientempires.images.*;

public class GameDrawAction extends GameDraw
{
	

	public static float mScale = 2.5f;
	public static int mA = (int) (Images.bitmapSize * mScale);
	private static float	actionBitmapH;
	private static float	actionBitmapW;
	
	public static Paint		whiteAlphaPaint		= new Paint();
	public static Paint		whitePaint			= new Paint();
	private static Paint	circlePaintExternal	= new Paint();
	private static Paint	circlePaintInternal	= new Paint();
	
	public static void initResources()
	{
		Bitmap defaultActionBitmap = ActionImages.getActionBitmap(ActionType.ACTION_UNIT_MOVE);
		
		GameDrawAction.actionBitmapH = defaultActionBitmap.getHeight();
		GameDrawAction.actionBitmapW = defaultActionBitmap.getWidth();
		
		GameDrawAction.whiteAlphaPaint.setColor(0x88FFFFFF);
		
		GameDrawAction.whitePaint.setColor(Color.WHITE);
		
		GameDrawAction.circlePaintExternal.setStyle(Style.FILL);
		GameDrawAction.circlePaintExternal.setColor(Color.CYAN);
		
		GameDrawAction.circlePaintInternal.setStyle(Style.FILL);
		GameDrawAction.circlePaintInternal.setColor(Color.BLUE);
	}
	
	private Bitmap[]		actionBitmaps	= new Bitmap[0];
	private ActionType[]	actionTypes		= new ActionType[0];
	private Point[]			actionsPoints	= new Point[0];
	public int				amount			= 0;
	
	private int		h;
	private int		w;
	private float	bitmapDeltaWidth;
	public boolean	isActive	= true;
	
	public GameDrawAction(GameDrawMain gameDraw)
	{
		super(gameDraw);
		
		this.w = GameView.w;
		this.h = gameDraw.gameDrawActionH;
	}
	
	public void update(int lastTapI, int lastTapJ)
	{
		final Action action = new Action(ActionType.GET_ACTIONS);
		action.setProperty("i", lastTapI);
		action.setProperty("j", lastTapJ);
		ActionResult result = Client.action(action);
		
		updateAsync(result);
	}
	
	public void updateAsync(ActionResult result)
	{
		final ActionType[] actionTypes = (ActionType[]) result.getProperty("actions");
		this.amount = actionTypes.length;
		
		// if (actionTypes.contains(ActionType.ACTION_UNIT_MOVE))
		// this.gameView.showNewUnitWay(this.gameView.lastTapI, this.gameView.lastTapJ);
		
		final Bitmap[] actionBitmaps = new Bitmap[this.amount];
		for (int i = 0; i < this.amount; i++)
		{
			ActionType actionType = actionTypes[i];
			final Bitmap actionBitmap = ActionImages.getActionBitmap(actionType);
			actionBitmaps[i] = actionBitmap;
		}
		updateActionBitmapsState(actionBitmaps, actionTypes);
	}
	
	public void updateActionBitmapsState(Bitmap[] actionBitmaps, ActionType[] actionTypes)
	{
		this.actionBitmaps = actionBitmaps;
		this.actionTypes = actionTypes;
		this.bitmapDeltaWidth = this.w / (this.actionBitmaps.length + 1);
		
		this.actionsPoints = new Point[this.amount];
		for (int i = 0; i < this.amount; i++)
		{
			int x = (int) (this.bitmapDeltaWidth * (i + 1) - GameDraw.A / 2.0f);
			int y = (int) (this.h - GameDrawAction.actionBitmapH) / 2;
			this.actionsPoints[i] = new Point(x, y);
		}
	}
	
	public void touch(float tapY, float tapX)
	{
		int i = Math.round(tapX / this.bitmapDeltaWidth);
		i--;
		if (i >= 0 && i < this.actionBitmaps.length)
			this.gameDraw.inputAlgorithmMain.performAction(this.actionTypes[i]);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.amount == 0)
			return;
			
		canvas.drawRect(0, 0, this.w, this.h, GameDrawAction.whiteAlphaPaint);
		for (int i = 0; i < this.amount; i++)
		{
			final Bitmap bitmap = this.actionBitmaps[i];
			final int x = this.actionsPoints[i].x;
			final int y = this.actionsPoints[i].y;
			canvas.drawBitmap(bitmap, x, y, null);
		}
	}
	
}
