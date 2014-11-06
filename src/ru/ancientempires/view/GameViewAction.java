package ru.ancientempires.view;

import java.util.ArrayList;

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
	
	private ArrayList<Bitmap>		actionBitmaps		= new ArrayList<Bitmap>();
	private ArrayList<ActionType>	actionTypes			= new ArrayList<ActionType>();
	private ArrayList<Point>		actionsPoints		= new ArrayList<Point>();
	
	private int						height;
	private int						width;
	float							bitmapDeltaWidth;
	
	private static float			actionBitmapH;
	private static float			actionBitmapW;
	
	public static Paint				whiteAlphaPaint		= new Paint();
	public static Paint				whitePaint			= new Paint();
	private static Paint			circlePaintExternal	= new Paint();
	private static Paint			circlePaintInternal	= new Paint();
	
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
		final Action action = new Action(ActionType.GET_AVAILABLE_ACTIONS_FOR_CELL);
		action.setProperty("i", this.gameView.lastTapI);
		action.setProperty("j", this.gameView.lastTapJ);
		
		final ActionResult result = Client.action(action);
		
		final int amount = (Integer) result.getProperty("amountActions");
		final ArrayList<ActionType> actionTypes = new ArrayList<ActionType>(amount);
		for (int k = 0; k < amount; k++)
		{
			final ActionType actionType = (ActionType) result.getProperty("action" + k);
			actionTypes.add(actionType);
		}
		
		// if (actionTypes.contains(ActionType.ACTION_UNIT_MOVE))
		// this.gameView.showNewUnitWay(this.gameView.lastTapI, this.gameView.lastTapJ);
		
		final ArrayList<Bitmap> actionBitmaps = new ArrayList<Bitmap>(amount);
		for (ActionType actionType : actionTypes)
		{
			final Bitmap actionBitmap = ActionImages.getActionBitmap(actionType);
			actionBitmaps.add(actionBitmap);
		}
		updateActionBitmapsState(actionBitmaps, actionTypes);
		return true;
	}
	
	public void updateActionBitmapsState(ArrayList<Bitmap> actionBitmaps, ArrayList<ActionType> actionTypes)
	{
		if (actionBitmaps.size() == 0)
			setVisibility(View.GONE);
		else
		{
			setVisibility(View.VISIBLE);
			this.actionBitmaps = actionBitmaps;
			this.actionTypes = actionTypes;
			this.bitmapDeltaWidth = this.width / (this.actionBitmaps.size() + 1);
			
			this.actionsPoints.clear();
			for (int i = 0; i < this.actionBitmaps.size(); i++)
			{
				int x = (int) (this.bitmapDeltaWidth * (i + 1) - GameViewAction.actionBitmapW / 2.0f);
				int y = (int) (this.height / 2 - GameViewAction.actionBitmapH / 2.0f);
				this.actionsPoints.add(new Point(x, y));
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
			float y = event.getY();
			
			int i = Math.round(x / this.bitmapDeltaWidth);
			i--;
			if (i >= 0 && i < this.actionBitmaps.size())
				this.gameView.performAction(this.actionTypes.get(i));
		}
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (!this.actionBitmaps.isEmpty())
		{
			canvas.drawRect(0, 0, this.width, this.height, GameViewAction.whiteAlphaPaint);
			for (int i = 0; i < this.actionBitmaps.size(); i++)
			{
				final Bitmap bitmap = this.actionBitmaps.get(i);
				final int x = this.actionsPoints.get(i).x;
				final int y = this.actionsPoints.get(i).y;
				
				// canvas.drawCircle(x + GameActionView.actionBitmapH / 2, y + GameActionView.actionBitmapW / 2,
				// GameActionView.actionBitmapH / 2 * 1.1f, GameActionView.circlePaintInternal);
				// canvas.drawCircle(x + GameActionView.actionBitmapH / 2, y + GameActionView.actionBitmapW / 2,
				// GameActionView.actionBitmapH / 2, GameActionView.whitePaint);
				
				canvas.drawBitmap(bitmap, x, y, null);
			}
		}
	}
	
}
