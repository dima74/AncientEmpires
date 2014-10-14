package ru.ancientempires;

import java.util.ArrayList;

import ru.ancientempires.action.ActionType;
import ru.ancientempires.images.ActionImages;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class GameActionView extends View
{
	
	public GameActionView(Context context)
	{
		super(context);
		
		setVisibility(View.GONE);
	}
	
	public GameView					gameView;
	
	private ArrayList<Bitmap>		actionBitmaps	= new ArrayList<Bitmap>();
	private ArrayList<ActionType>	actionTypes		= new ArrayList<ActionType>();
	private ArrayList<Point>		actionsPoints	= new ArrayList<Point>();
	
	private int						height;
	private int						width;
	float							bitmapDeltaWidth;
	
	private static float			actionBitmapHeight;
	private static float			actionBitmapWidth;
	
	private static Paint			whitePaint		= new Paint();
	
	public static void initResources()
	{
		Bitmap defaultActionBitmap = ActionImages.getActionBitmap(ActionType.ACTION_UNIT_MOVE);
		
		GameActionView.actionBitmapHeight = defaultActionBitmap.getHeight();
		GameActionView.actionBitmapWidth = defaultActionBitmap.getWidth();
		
		GameActionView.whitePaint.setARGB(0xBB, 0xFF, 0xFF, 0xFF);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		this.height = h;
		this.width = w;
		
		updateActionBitmapsState(this.actionBitmaps, this.actionTypes);
		
		super.onSizeChanged(w, h, oldw, oldh);
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
				int x = (int) (this.bitmapDeltaWidth * (i + 1) - GameActionView.actionBitmapWidth / 2.0f);
				int y = (int) (this.height / 2 - GameActionView.actionBitmapHeight / 2.0f);
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
			canvas.drawRect(0, 0, this.width, this.height, GameActionView.whitePaint);
			for (int i = 0; i < this.actionBitmaps.size(); i++)
			{
				Bitmap bitmap = this.actionBitmaps.get(i);
				canvas.drawBitmap(bitmap, this.actionsPoints.get(i).x, this.actionsPoints.get(i).y, null);
			}
		}
	}
	
}
