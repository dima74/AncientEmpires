package ru.ancientempires.view.draws.onframes;

import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.helpers.Point;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.GameDrawUnit;
import android.graphics.Canvas;

public class GameDrawUnitMove extends GameDrawOnFrames
{
	
	public static int	framesForCell;		// ii-2, player-4
	private int			framesForCellMy;
	
	private UnitBitmap	unitBitmap;
	private int			i, j;
	private Point[]		ways;
	private boolean		isStay	= false;
	
	public GameDrawUnitMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
		this.framesForCellMy = GameDrawUnitMove.framesForCell;
	}
	
	public void init(int i, int j)
	{
		this.unitBitmap = GameHandler.checkCoord(i, j) ? this.gameDraw.gameDrawUnit.extractUnit(i, j) : GameDrawUnit.getUnitBitmap(GameHandler.getUnit(i, j));
		this.i = i;
		this.j = j;
		this.isStay = true;
	}
	
	public void start(Point[] ways, ActionResult result)
	{
		this.isStay = false;
		this.ways = ways;
		animate(0, (ways.length - 1) * this.framesForCellMy);
		if (result != null)
			this.gameDraw.gameDrawUnitMoveEnd.start(result, this.frameLength);
	}
	
	public void destroy()
	{
		if (!this.isStay && this.unitBitmap != null)
		{
			Point endPoint = this.ways[this.ways.length - 1];
			if (GameHandler.checkCoord(endPoint.i, endPoint.j))
				this.gameDraw.gameDrawUnit.updateOneUnit(endPoint.i, endPoint.j);
		}
		this.unitBitmap = null;
		this.isStay = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		if (this.isStay)
			drawUnit(canvas, this.i * GameDraw.A, this.j * GameDraw.A);
		super.draw(canvas);
		if (!this.isDrawing)
			return;
		
		int framePass = this.gameDraw.iFrame - this.frameStart;
		int i = framePass / this.framesForCellMy;
		int framePassPart = framePass - i * this.framesForCellMy;
		int frameLeftPart = this.framesForCellMy - framePassPart;
		int y = (frameLeftPart * this.ways[i].i + framePassPart * this.ways[i + 1].i) * GameDraw.A / this.framesForCellMy;
		int x = (frameLeftPart * this.ways[i].j + framePassPart * this.ways[i + 1].j) * GameDraw.A / this.framesForCellMy;
		drawUnit(canvas, y, x);
		MyLog.l(System.currentTimeMillis() + " " + y / 3 * 2);
		
		if (this.gameDraw.iFrame == this.frameEnd - 1)
			destroy();
	}
	
	private void drawUnit(Canvas canvas, int y, int x)
	{
		canvas.drawBitmap(this.unitBitmap.getBitmap(), x, y, null);
		canvas.drawBitmap(this.unitBitmap.textBitmap, x, y, null);
	}
	
}
