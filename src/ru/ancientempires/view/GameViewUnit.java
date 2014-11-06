package ru.ancientempires.view;

import java.io.IOException;

import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.View;

public class GameViewUnit extends GameViewPart
{
	
	public GameViewUnit(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private static SomeWithBitmaps	cursorWay		= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorAttack	= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorPointer	= new SomeWithBitmaps();
	
	public static void init() throws IOException
	{
		GameViewUnit.cursorWay.setBitmaps(new String[]
		{
				"cursor_way.png"
		});
		GameViewUnit.cursorAttack.setBitmaps(new String[]
		{
				"cursor_attack.png"
		});
		GameViewUnit.cursorPointer.setBitmaps(new String[]
		{
				"cursor_pointer_0.png",
				"cursor_pointer_1.png",
				"cursor_pointer_2.png"
		});
	}
	
	private Unit[][]	field;
	
	public GameViewUnit setField(Unit[][] field)
	{
		this.field = field;
		return this;
	}
	
	private boolean		isWayVisible	= false;
	private boolean		isAttackVisible	= false;
	
	private int			lastUnitI;
	private int			lastUnitJ;
	
	private ZoneView	wayView;
	
	public GameViewUnit setWayView(ZoneView view)
	{
		this.wayView = view;
		this.wayView.setBitmap(GameViewUnit.cursorWay.getBitmap());
		return this;
	}
	
	private ZoneView	attackView;
	
	public GameViewUnit setAttackView(ZoneView view)
	{
		this.attackView = view;
		this.attackView.setBitmap(GameViewUnit.cursorAttack.getBitmap());
		return this;
	}
	
	private int			radius;
	private int			size;
	
	private boolean[][]	fieldWay;
	private boolean[][]	fieldAttack;
	private boolean[][]	realFieldAttack;
	
	@Override
	public boolean update()
	{
		int i = this.gameView.lastTapI;
		int j = this.gameView.lastTapJ;
		
		if (this.gameView.isWayVisible)
			if (this.isWayVisible)
			{
				this.gameView.isWayVisible = false;
				this.isWayVisible = false;
				this.wayView.setVisibility(View.GONE);
				
				int relI = this.radius + i - this.lastUnitI;
				int relJ = this.radius + j - this.lastUnitJ;
				boolean contains = relI >= 0 && relI < this.size && relJ >= 0 && relJ < this.size
					&& this.fieldWay[relI][relJ];
				
				if (contains)
				{
					final Action action = new Action(ActionType.ACTION_UNIT_MOVE);
					action.setProperty("oldI", this.lastUnitI);
					action.setProperty("oldJ", this.lastUnitJ);
					action.setProperty("newI", i);
					action.setProperty("newJ", j);
					Client.action(action);
					
					invalidate();
					return true;
				}
				else
					return false;
			}
			else
			{
				this.lastUnitI = i;
				this.lastUnitJ = j;
				
				Action action = new Action(ActionType.GET_UNIT_WAY);
				action.setProperty("i", i);
				action.setProperty("j", j);
				
				new AsyncTask<Action, Void, ActionResult>()
				{
					@Override
					protected ActionResult doInBackground(Action... params)
					{
						return Client.action(params[0]);
					}
					
					@Override
					protected void onPostExecute(ActionResult result)
					{
						performAnimateWay(result);
					}
				}.execute(action);
				
				return true;
			}
		else
		{
			this.wayView.setVisibility(View.GONE);
			this.isWayVisible = false;
		}
		
		if (this.gameView.isAttackVisible)
			if (this.isAttackVisible)
			{
				this.gameView.isAttackVisible = false;
				this.isAttackVisible = false;
				this.attackView.setVisibility(View.GONE);
				
				int relI = this.radius + i - this.lastUnitI;
				int relJ = this.radius + j - this.lastUnitJ;
				boolean contains = relI >= 0 && relI < this.size && relJ >= 0 && relJ < this.size
					&& this.realFieldAttack[relI][relJ];
				
				if (contains)
				{
					final Action action = new Action(ActionType.ACTION_UNIT_ATTACK);
					action.setProperty("attackingI", this.lastUnitI);
					action.setProperty("attackingJ", this.lastUnitJ);
					action.setProperty("attackedI", i);
					action.setProperty("attackedJ", j);
					Client.action(action);
					
					invalidate();
					return true;
				}
				else
					return false;
			}
			else
			{
				this.lastUnitI = i;
				this.lastUnitJ = j;
				
				Action action = new Action(ActionType.GET_UNIT_ATTACK);
				action.setProperty("i", i);
				action.setProperty("j", j);
				
				new AsyncTask<Action, Void, ActionResult>()
				{
					@Override
					protected ActionResult doInBackground(Action... params)
					{
						return Client.action(params[0]);
					}
					
					@Override
					protected void onPostExecute(ActionResult result)
					{
						performAnimateAttack(result);
					}
				}.execute(action);
				
				return true;
			}
		else
		{
			this.attackView.setVisibility(View.GONE);
			this.isAttackVisible = false;
		}
		
		return false;
	}
	
	private void performAnimateWay(ActionResult result)
	{
		MyLog.log("GameViewUnit.performAnimate()");
		this.isWayVisible = true;
		this.wayView.setVisibility(View.VISIBLE);
		
		this.fieldWay = (boolean[][]) result.getProperty("is");
		this.size = this.fieldWay.length;
		this.radius = this.size / 2;
		
		this.wayView.setRadius(this.radius).setZone(this.fieldWay);
		this.wayView.startAnimate();
		updateOffset();
	}
	
	private void performAnimateAttack(ActionResult result)
	{
		this.isAttackVisible = true;
		this.attackView.setVisibility(View.VISIBLE);
		
		this.fieldAttack = (boolean[][]) result.getProperty("all");
		this.realFieldAttack = (boolean[][]) result.getProperty("real");
		this.size = this.fieldAttack.length;
		this.radius = this.size / 2;
		
		this.attackView.setRadius(this.radius).setZone(this.fieldAttack);
		this.attackView.startAnimate();
		updateOffset();
	}
	
	@Override
	public void updateOffset()
	{
		this.wayView.setY(this.gameView.offsetY + (this.lastUnitI - this.radius) * GameView.baseH);
		this.wayView.setX(this.gameView.offsetX + (this.lastUnitJ - this.radius) * GameView.baseW);
		this.attackView.setY(this.gameView.offsetY + (this.lastUnitI - this.radius) * GameView.baseH);
		this.attackView.setX(this.gameView.offsetX + (this.lastUnitJ - this.radius) * GameView.baseW);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(this.gameView.offsetX, this.gameView.offsetY);
		// юниты
		for (int i = 0; i < this.field.length; i++)
			for (int j = 0; j < this.field[i].length; j++)
			{
				final Unit unit = this.field[i][j];
				if (unit != null)
				{
					final int y = GameView.baseH * i;
					final int x = GameView.baseW * j;
					
					final Bitmap bitmapUnit = UnitImages.getUnitBitmap(unit);
					canvas.drawBitmap(bitmapUnit, x, y, null);
					
					final int health = Math.round(unit.health * 100);
					
					final int one = health / 10;
					final int two = health % 10;
					
					final int textX = x;
					final int textY = y + GameView.baseH - NumberImages.height;
					
					if (one == 0)
						canvas.drawBitmap(NumberImages.getNumberBitmap(two), textX, textY, null);
					else
					{
						canvas.drawBitmap(NumberImages.getNumberBitmap(one), textX, textY, null);
						canvas.drawBitmap(NumberImages.getNumberBitmap(two), textX + NumberImages.width, textY, null);
					}
				}
			}
	}
	
}
