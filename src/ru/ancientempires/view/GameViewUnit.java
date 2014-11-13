package ru.ancientempires.view;

import java.io.IOException;

import ru.ancientempires.AbstractActionAsyncTask;
import ru.ancientempires.SomeWithBitmaps;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.images.NumberImages;
import ru.ancientempires.images.UnitImages;
import ru.ancientempires.model.Unit;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameViewUnit extends GameViewPart
{
	
	private static final Paint	LINE_PAINT	= new Paint();
	
	public GameViewUnit(Context context, GameView gameView)
	{
		super(context, gameView);
		GameViewUnit.LINE_PAINT.setStrokeWidth(GameView.baseH / 3);
		GameViewUnit.LINE_PAINT.setColor(0xffe10052);
	}
	
	private static SomeWithBitmaps	cursorWay		= new SomeWithBitmaps();
	private static SomeWithBitmaps	cursorAttack	= new SomeWithBitmaps();
	
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
	}
	
	private GameViewCursor	gameViewCursor;
	
	public GameViewUnit setGameViewCursor(GameViewCursor gameViewCursor)
	{
		this.gameViewCursor = gameViewCursor;
		return this;
	}
	
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
	
	private Unit[][]	field;
	private int			fieldH;
	private int			fieldW;
	
	public GameViewUnit setField(Unit[][] field)
	{
		this.field = field;
		this.fieldH = field.length;
		this.fieldW = field[0].length;
		return this;
	}
	
	private boolean		isWayVisible	= false;
	private boolean		isAttackVisible	= false;
	
	private boolean		wayChanged;
	private boolean		attackChanged;
	
	private int			lastUnitI;
	private int			lastUnitJ;
	
	private int			radius;
	private int			size;
	
	private boolean[][]	fieldWay;
	private boolean[][]	fieldAttack;
	private boolean[][]	realFieldAttack;
	private int[][]		fieldPrevI;
	private int[][]		fieldPrevJ;
	
	private int			wayLength;
	private float[]		wayPoints;
	
	@Override
	public boolean performAction(ActionType actionType)
	{
		int i = this.gameView.lastTapI;
		int j = this.gameView.lastTapJ;
		if (actionType == ActionType.ACTION_UNIT_MOVE)
		{
			if (this.isAttackVisible)
			{
				hideAttack();
				this.gameView.lastTapI = this.lastUnitI;
				this.gameView.lastTapJ = this.lastUnitJ;
				i = this.lastUnitI;
				j = this.lastUnitJ;
				this.gameView.gameViewCursor.update();
			}
			if (this.isWayVisible)
			{
				if (this.wayChanged)
				{
					final Action action = new Action(ActionType.ACTION_UNIT_MOVE);
					action.setProperty("oldI", this.lastUnitI);
					action.setProperty("oldJ", this.lastUnitJ);
					action.setProperty("newI", i);
					action.setProperty("newJ", j);
					Client.action(action);
					
					invalidate();
				}
				hideWay();
				return true;
			}
			else
			{
				this.lastUnitI = i;
				this.lastUnitJ = j;
				
				Action action = new Action(ActionType.GET_UNIT_WAY);
				action.setProperty("i", i);
				action.setProperty("j", j);
				
				new AbstractActionAsyncTask()
				{
					@Override
					protected void onPostExecute(ActionResult result)
					{
						performAnimateWay(result);
					};
				}.execute(action);
				return false;
			}
		}
		else if (actionType == ActionType.ACTION_UNIT_ATTACK)
		{
			if (this.isWayVisible)
			{
				hideWay();
				this.gameView.lastTapI = this.lastUnitI;
				this.gameView.lastTapJ = this.lastUnitJ;
				i = this.lastUnitI;
				j = this.lastUnitJ;
				this.gameView.gameViewCursor.update();
			}
			if (this.isAttackVisible)
			{
				if (this.attackChanged)
				{
					final Action action = new Action(ActionType.ACTION_UNIT_ATTACK);
					action.setProperty("attackingI", this.lastUnitI);
					action.setProperty("attackingJ", this.lastUnitJ);
					action.setProperty("attackedI", i);
					action.setProperty("attackedJ", j);
					Client.action(action);
					
					invalidate();
				}
				hideAttack();
				return true;
			}
			else
			{
				this.lastUnitI = i;
				this.lastUnitJ = j;
				
				Action action = new Action(ActionType.GET_UNIT_ATTACK);
				action.setProperty("i", i);
				action.setProperty("j", j);
				
				new AbstractActionAsyncTask()
				{
					@Override
					protected void onPostExecute(ActionResult result)
					{
						performAnimateAttack(result);
					};
				}.execute(action);
				return false;
			}
		}
		else
		{
			if (this.isWayVisible)
				hideWay();
			if (this.isAttackVisible)
				hideAttack();
			return true;
		}
	}
	
	private void hideWay()
	{
		this.isWayVisible = false;
		this.wayChanged = false;
		this.wayView.isInverse = true;
		this.wayView.startAnimate();
		this.gameView.isWayVisible = false;
		this.gameViewCursor.hideCursorWay();
	}
	
	private void hideAttack()
	{
		this.isAttackVisible = false;
		this.attackChanged = false;
		this.attackView.isInverse = true;
		this.attackView.startAnimate();
		this.gameView.isAttackVisible = false;
		this.gameViewCursor.hideCursorAttack();
	}
	
	private void performAnimateWay(ActionResult result)
	{
		this.isWayVisible = true;
		this.fieldWay = (boolean[][]) result.getProperty("is");
		this.fieldPrevI = (int[][]) result.getProperty("prevI");
		this.fieldPrevJ = (int[][]) result.getProperty("prevJ");
		performAnimateZone(this.wayView, this.fieldWay);
	}
	
	private void performAnimateAttack(ActionResult result)
	{
		this.isAttackVisible = true;
		this.fieldAttack = (boolean[][]) result.getProperty("all");
		this.realFieldAttack = (boolean[][]) result.getProperty("real");
		performAnimateZone(this.attackView, this.fieldAttack);
	}
	
	private void performAnimateZone(ZoneView zoneView, boolean[][] field)
	{
		this.size = field.length;
		this.radius = this.size / 2;
		zoneView.isInverse = false;
		zoneView.setRadius(this.radius).setZone(field);
		zoneView.startAnimate();
		updateOffset();
	}
	
	@Override
	public boolean update()
	{
		int i = this.gameView.lastTapI;
		int j = this.gameView.lastTapJ;
		
		int relI = this.radius + i - this.lastUnitI;
		int relJ = this.radius + j - this.lastUnitJ;
		boolean boundsNorm = relI >= 0 && relI < this.size && relJ >= 0 && relJ < this.size;
		if (this.isWayVisible)
			if (boundsNorm && this.fieldWay[relI][relJ])
			{
				this.wayChanged = true;
				this.gameViewCursor.setCursorWay();
				updateWayLine(relI, relJ);
			}
			else
				hideWay();
		if (this.isAttackVisible)
			if (boundsNorm && this.realFieldAttack[relI][relJ])
			{
				this.attackChanged = true;
				this.gameViewCursor.setCursorAttack();
			}
			else
				hideAttack();
		
		return this.wayChanged || this.attackChanged;
	}
	
	private void updateWayLine(final int relI, final int relJ)
	{
		this.wayLength = 0;
		int i = relI;
		int j = relJ;
		while (!(i == this.radius && j == this.radius))
		{
			this.wayLength++;
			final int newI = this.fieldPrevI[i][j];
			final int newJ = this.fieldPrevJ[i][j];
			i = newI;
			j = newJ;
		}
		
		int[] wayYs = new int[this.wayLength + 1];
		int[] wayXs = new int[this.wayLength + 1];
		i = relI;
		j = relJ;
		for (int k = this.wayLength; k >= 0; k--)
		{
			float relToAbsI = -this.radius + this.lastUnitI + 0.5f;
			float relToAbsJ = -this.radius + this.lastUnitJ + 0.5f;
			wayYs[k] = (int) ((i + relToAbsI) * GameView.baseH);
			wayXs[k] = (int) ((j + relToAbsJ) * GameView.baseW);
			final int newI = this.fieldPrevI[i][j];
			final int newJ = this.fieldPrevJ[i][j];
			i = newI;
			j = newJ;
		}
		
		int wayLengthNew = 2 * this.wayLength;
		this.wayPoints = new float[wayLengthNew * 2];
		
		// extra 1/12 pixels
		for (int k = 0; k < this.wayLength; k++)
		{
			int y1 = wayYs[k];
			int x1 = wayXs[k];
			int y2 = wayYs[k + 1];
			int x2 = wayXs[k + 1];
			if (wayYs[k] == wayYs[k + 1])
				if (wayXs[k] < wayXs[k + 1])
				{
					x1 -= GameView.baseW / 12;
					x2 += GameView.baseW / 12;
				}
				else
				{
					x2 -= GameView.baseW / 12;
					x1 += GameView.baseW / 12;
				}
			else if (wayXs[k] == wayXs[k + 1])
				if (wayYs[k] < wayYs[k + 1])
				{
					y1 -= GameView.baseH / 12;
					y2 += GameView.baseH / 12;
				}
				else
				{
					y2 -= GameView.baseH / 12;
					y1 += GameView.baseH / 12;
				}
			this.wayPoints[4 * k + 0] = x1;
			this.wayPoints[4 * k + 1] = y1;
			this.wayPoints[4 * k + 2] = x2;
			this.wayPoints[4 * k + 3] = y2;
		}
		invalidate();
	}
	
	@Override
	public void updateOffset()
	{
		int y = this.gameView.offsetY + (this.lastUnitI - this.radius) * GameView.baseH;
		int x = this.gameView.offsetX + (this.lastUnitJ - this.radius) * GameView.baseW;
		if (this.isWayVisible)
		{
			this.wayView.setY(y);
			this.wayView.setX(x);
		}
		if (this.isAttackVisible)
		{
			this.attackView.setY(y);
			this.attackView.setX(x);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.translate(this.gameView.offsetX, this.gameView.offsetY);
		// юниты
		for (int i = 0; i < this.fieldH; i++)
			for (int j = 0; j < this.fieldW; j++)
				drawUnit(canvas, i, j);
		
		if (this.wayChanged)
			canvas.drawLines(this.wayPoints, GameViewUnit.LINE_PAINT);
		
		drawUnit(canvas, this.lastUnitI, this.lastUnitJ);
	}
	
	private void drawUnit(Canvas canvas, int i, int j)
	{
		final Unit unit = this.field[i][j];
		if (unit == null)
			return;
		final int y = GameView.baseH * i;
		final int x = GameView.baseW * j;
		
		final Bitmap bitmapUnit = UnitImages.getUnitBitmap(unit);
		canvas.drawBitmap(bitmapUnit, x, y, null);
		
		final int health = Math.round(unit.health * 100);
		final int one = health / 10;
		final int two = health % 10;
		
		final int textX = x;
		final int textY = y + GameView.baseH - NumberImages.height;
		
		final Bitmap bitmapOne = NumberImages.getNumberBitmap(one);
		final Bitmap bitmapTwo = NumberImages.getNumberBitmap(two);
		if (one == 0)
			canvas.drawBitmap(bitmapTwo, textX, textY, null);
		else
		{
			canvas.drawBitmap(bitmapOne, textX, textY, null);
			canvas.drawBitmap(bitmapTwo, textX + NumberImages.width, textY, null);
		}
	}
	
}
