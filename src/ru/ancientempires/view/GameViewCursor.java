package ru.ancientempires.view;

import java.io.IOException;

import ru.ancientempires.SomeWithBitmaps;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class GameViewCursor extends GameViewPart
{
	
	public GameViewCursor(Context context, GameView gameView)
	{
		super(context, gameView);
	}
	
	private static final SomeWithBitmaps	cursor				= new SomeWithBitmaps();
	private static final SomeWithBitmaps	cursorPointerAttack	= new SomeWithBitmaps();
	private static final SomeWithBitmaps	cursorPointerWay	= new SomeWithBitmaps();
	
	private static int						cursorH;
	private static int						cursorW;
	private static int						cursorWayH;
	private static int						cursorWayW;
	private static int						cursorAttackH;
	private static int						cursorAttackW;
	
	public static void init() throws IOException
	{
		GameViewCursor.cursor.setBitmaps(new String[]
		{
				"cursor_down.png",
				"cursor_up.png"
		});
		GameViewCursor.cursorPointerAttack.setBitmaps(new String[]
		{
				"cursor_pointer_attack_0.png",
				"cursor_pointer_attack_1.png",
				"cursor_pointer_attack_2.png"
		});
		GameViewCursor.cursorPointerWay.setBitmaps(new String[]
		{
				"cursor_pointer_way.png"
		});
		GameViewCursor.cursorH = GameViewCursor.cursor.getBitmap().getHeight();
		GameViewCursor.cursorW = GameViewCursor.cursor.getBitmap().getWidth();
		GameViewCursor.cursorWayH = GameViewCursor.cursorPointerWay.getBitmap().getHeight();
		GameViewCursor.cursorWayW = GameViewCursor.cursorPointerWay.getBitmap().getWidth();
		GameViewCursor.cursorAttackH = GameViewCursor.cursorPointerAttack.getBitmap().getHeight();
		GameViewCursor.cursorAttackW = GameViewCursor.cursorPointerAttack.getBitmap().getWidth();
	}
	
	private GameViewUnit	gameViewUnit;
	
	public GameViewCursor setGameViewUnit(GameViewUnit gameViewUnit)
	{
		this.gameViewUnit = gameViewUnit;
		return this;
	}
	
	private boolean	isCursorVisible			= false;
	private boolean	isCursorWayVisible		= false;
	private boolean	isCursorAttackVisible	= false;
	private int		cursorI;
	private int		cursorJ;
	
	@Override
	public boolean update()
	{
		this.cursorI = this.gameView.lastTapI;
		this.cursorJ = this.gameView.lastTapJ;
		this.isCursorVisible = true;
		invalidate();
		return false;
	}
	
	public void setCursorWay()
	{
		this.isCursorWayVisible = true;
		invalidate();
	}
	
	public void setCursorAttack()
	{
		this.isCursorAttackVisible = true;
		invalidate();
	}
	
	public void hideCursorWay()
	{
		this.isCursorWayVisible = false;
		invalidate();
	}
	
	public void hideCursorAttack()
	{
		this.isCursorAttackVisible = false;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		// рисуем курсор (если есть)
		canvas.translate(
				this.gameView.offsetX + GameView.baseH / 2,
				this.gameView.offsetY + GameView.baseW / 2);
		
		if (this.isCursorWayVisible)
		{
			final Bitmap bitmap = GameViewCursor.cursorPointerWay.getBitmap();
			final int y = GameView.baseH * this.cursorI - GameViewCursor.cursorWayH / 2;
			final int x = GameView.baseW * this.cursorJ - GameViewCursor.cursorWayW / 2;
			canvas.drawBitmap(bitmap, x, y, null);
		}
		if (this.isCursorAttackVisible)
		{
			final Bitmap bitmap = GameViewCursor.cursorPointerAttack.getBitmap();
			final int y = GameView.baseH * this.cursorI - GameViewCursor.cursorAttackH / 2;
			final int x = GameView.baseW * this.cursorJ - GameViewCursor.cursorAttackW / 2;
			canvas.drawBitmap(bitmap, x, y, null);
		}
		if (this.isCursorVisible && !this.isCursorAttackVisible)
		{
			final Bitmap bitmap = GameViewCursor.cursor.getBitmap();
			final int y = GameView.baseH * this.cursorI - GameViewCursor.cursorH / 2;
			final int x = GameView.baseW * this.cursorJ - GameViewCursor.cursorW / 2;
			canvas.drawBitmap(bitmap, x, y, null);
		}
	}
	
}
