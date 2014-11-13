package ru.ancientempires.view;

import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.ActionImages;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class GameViewAction extends GameViewPart
{
	
	public GameViewAction(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private Bitmap[]		actionBitmaps		= new Bitmap[0];
	private ActionType[]	actionTypes			= new ActionType[0];
	private Point[]			actionsPoints		= new Point[0];
	private int				amount				= 0;
	
	private int				height;
	private int				width;
	private float			bitmapDeltaWidth;
	
	private static float	actionBitmapH;
	private static float	actionBitmapW;
	
	public static Paint		whiteAlphaPaint		= new Paint();
	public static Paint		whitePaint			= new Paint();
	private static Paint	circlePaintExternal	= new Paint();
	private static Paint	circlePaintInternal	= new Paint();
	
	public static void initResources()
	{
		Bitmap defaultActionBitmap = ActionImages.getActionBitmap(ActionType.ACTION_UNIT_MOVE);
		
		GameViewAction.actionBitmapH = defaultActionBitmap.getHeight();
		GameViewAction.actionBitmapW = defaultActionBitmap.getWidth();
		
		GameViewAction.whiteAlphaPaint.setColor(0x88FFFFFF);
		
		GameViewAction.whitePaint.setColor(Color.WHITE);
		
		GameViewAction.circlePaintExternal.setStyle(Style.FILL);
		GameViewAction.circlePaintExternal.setColor(Color.CYAN);
		
		GameViewAction.circlePaintInternal.setStyle(Style.FILL);
		GameViewAction.circlePaintInternal.setColor(Color.BLUE);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		this.height = h;
		this.width = w;
		
		updateActionBitmapsState(this.actionBitmaps, this.actionTypes);
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	public boolean update()
	{
		final Action action = new Action(ActionType.GET_ACTIONS);
		action.setProperty("i", this.gameView.lastTapI);
		action.setProperty("j", this.gameView.lastTapJ);
		
		final ActionResult result = Client.action(action);
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
		return true;
	}
	
	public void updateActionBitmapsState(Bitmap[] actionBitmaps, ActionType[] actionTypes)
	{
		if (this.amount == 0)
			setVisibility(View.GONE);
		else
		{
			setVisibility(View.VISIBLE);
			this.actionBitmaps = actionBitmaps;
			this.actionTypes = actionTypes;
			this.bitmapDeltaWidth = this.width / (this.actionBitmaps.length + 1);
			
			this.actionsPoints = new Point[this.amount];
			for (int i = 0; i < this.amount; i++)
			{
				int x = (int) (this.bitmapDeltaWidth * (i + 1) - GameViewAction.actionBitmapW / 2.0f);
				int y = (int) (this.height / 2 - GameViewAction.actionBitmapH / 2.0f);
				this.actionsPoints[i] = new Point(x, y);
			}
		}
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			float x = event.getX();
			
			int i = Math.round(x / this.bitmapDeltaWidth);
			i--;
			if (i >= 0 && i < this.actionBitmaps.length)
				this.gameView.performAction(this.actionTypes[i]);
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (!(this.amount == 0))
		{
			canvas.drawRect(0, 0, this.width, this.height, GameViewAction.whiteAlphaPaint);
			for (int i = 0; i < this.amount; i++)
			{
				final Bitmap bitmap = this.actionBitmaps[i];
				final int x = this.actionsPoints[i].x;
				final int y = this.actionsPoints[i].y;
				canvas.drawBitmap(bitmap, x, y, null);
			}
		}
	}
	
}
