package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import android.graphics.Canvas;

public class GameDrawUnitMove extends GameDrawOnFrames
{
	
	private static final int	FRAMES_FOR_CELL	= 7;
	
	private UnitBitmap			unitBitmap;
	private Point[]				ways;
	private boolean				isMoving		= false;
	
	public GameDrawUnitMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void init(int i, int j)
	{
		this.unitBitmap = this.gameDraw.gameDrawUnit.extractUnit(i, j);
		this.isMoving = false;
		this.ways = new Point[]
		{
				new Point(i, j)
		};
	}
	
	public void start(Point[] ways, ActionResult result)
	{
		this.isMoving = true;
		this.ways = ways;
		animate(0, (ways.length - 1) * GameDrawUnitMove.FRAMES_FOR_CELL);
		this.gameDraw.gameDrawUnitMoveEnd.start(result, this.frameLength);
	}
	
	public void end()
	{
		this.gameDraw.gameDrawUnit.updateOneUnit(this.ways[this.ways.length - 1]);
		destroy();
	}
	
	public void destroy()
	{
		this.unitBitmap = null;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.unitBitmap == null)
			return;
		if (!this.isMoving)
		{
			drawUnit(canvas, 0);
			return;
		}
		super.draw(canvas);
		
		if (!this.isDrawing)
		{
			drawUnit(canvas, this.ways.length - 1);
			end();
			return;
		}
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int i = framePass / GameDrawUnitMove.FRAMES_FOR_CELL;
		int framePassPart = framePass - i * GameDrawUnitMove.FRAMES_FOR_CELL;
		int frameLeftPart = GameDrawUnitMove.FRAMES_FOR_CELL - framePassPart;
		int y = (frameLeftPart * this.ways[i].i + framePassPart * this.ways[i + 1].i) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
		int x = (frameLeftPart * this.ways[i].j + framePassPart * this.ways[i + 1].j) * GameDraw.A / GameDrawUnitMove.FRAMES_FOR_CELL;
		drawUnit(canvas, y, x);
	}
	
	private void drawUnit(Canvas canvas, int y, int x)
	{
		canvas.drawBitmap(this.unitBitmap.getBitmap(), x, y, null);
		canvas.drawBitmap(this.unitBitmap.textBitmap, x, y, null);
	}
	
	private void drawUnit(Canvas canvas, int i)
	{
		drawUnit(canvas, this.ways[i].i * GameDraw.A, this.ways[i].j * GameDraw.A);
	}
	
}