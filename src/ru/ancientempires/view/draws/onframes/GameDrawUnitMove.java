package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.Point;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.framework.MyLog;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawMain;
import ru.ancientempires.view.draws.GameDrawUnits;

public class GameDrawUnitMove extends GameDrawOnFramesWithRangeValues
{
	
	public static int	framesForCell;										// ii-2, player-4
	private int			framesForCellMy	= GameDrawUnitMove.framesForCell;
	
	private UnitBitmap	unitBitmap;
	private Point[]		points;
	private Point		endPoint;
	
	public GameDrawUnitMove(GameDrawMain gameDraw)
	{
		super(gameDraw);
	}
	
	public void init(int i, int j)
	{
		this.unitBitmap = new UnitBitmap(GameHandler.getUnit(i, j));
		this.unitBitmap.keepTurn = true;
		if (GameHandler.checkCoord(i, j))
			this.gameDraw.gameDrawUnits.field[i][j] = this.unitBitmap;
	}
	
	public void start(Point[] points, ActionResult result)
	{
		this.endPoint = points[points.length - 1];
		this.gameDraw.gameDrawUnits.field[this.endPoint.i][this.endPoint.j] = this.unitBitmap;
		this.points = points;
		animateRange(0, (points.length - 1) * this.framesForCellMy - 1);
		if (result != null)
			this.gameDraw.gameDrawUnitMoveEnd.start(result, this.frameCount);
		this.gameDraw.gameDrawUnitsDead.keep[this.endPoint.i][this.endPoint.j] = true;
	}
	
	public void destroy()
	{
		if (this.unitBitmap != null)
			this.unitBitmap.keepTurn = false;
		this.unitBitmap = null;
	}
	
	@Override
	public void draw(Canvas canvas, int value)
	{
		if (this.unitBitmap == null)
			return;
		int i = value / this.framesForCellMy;
		int iPart = value % this.framesForCellMy;
		this.unitBitmap.y = (this.points[i].i * (this.framesForCellMy - 1 - iPart) + this.points[i + 1].i * iPart) * GameDraw.A / (this.framesForCellMy - 1);
		this.unitBitmap.x = (this.points[i].j * (this.framesForCellMy - 1 - iPart) + this.points[i + 1].j * iPart) * GameDraw.A / (this.framesForCellMy - 1);
		MyLog.l(value, this.unitBitmap.y, this.unitBitmap.x);
		GameDrawUnits.drawUnit(canvas, this.unitBitmap);
	}
	
	@Override
	public void onEndDraw()
	{
		destroy();
		this.gameDraw.gameDrawUnitsDead.keep[this.endPoint.i][this.endPoint.j] = false;
		GameActivity.gameView.thread.needUpdateCampaign = true;
	}
	
}
