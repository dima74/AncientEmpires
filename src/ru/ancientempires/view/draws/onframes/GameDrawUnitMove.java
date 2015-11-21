package ru.ancientempires.view.draws.onframes;

import android.graphics.Canvas;
import ru.ancientempires.Point;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.handlers.GameHandler;
import ru.ancientempires.activity.GameActivity;
import ru.ancientempires.images.bitmaps.UnitBitmap;
import ru.ancientempires.view.draws.GameDraw;

public class GameDrawUnitMove extends GameDrawOnFramesWithRangeValues
{
	
	public static int	framesForCell	= 8;								// ii-2, player-4
	private int			framesForCellMy	= GameDrawUnitMove.framesForCell;
	
	private UnitBitmap	unitBitmap;
	private Point[]		points;
	private int			endI;
	private int			endJ;
	
	public void init(int i, int j)
	{
		unitBitmap = GameHandler.checkCoord(i, j)
				? GameDraw.main.gameDrawUnits.field[i][j]
				: new UnitBitmap(GameHandler.getUnit(i, j));
		unitBitmap.keepTurn = true;
	}
	
	public void start(Point[] points, ActionResult result)
	{
		endI = points[points.length - 1].i;
		endJ = points[points.length - 1].j;
		if (GameHandler.checkCoord(endI, endJ))
		{
			GameDraw.main.gameDrawUnits.field[endI][endJ] = unitBitmap;
			GameDraw.main.gameDrawUnitsDead.keep[endI][endJ] = true;
		}
		GameDraw.main.gameDrawUnits.moveUnits.add(unitBitmap);
		
		this.points = points;
		animateRange(0, (points.length - 1) * framesForCellMy - 1);
		if (result != null)
			GameDraw.main.gameDrawUnitMoveEnd.start(result, frameCount);
	}
	
	public void destroy()
	{
		if (unitBitmap != null)
			unitBitmap.keepTurn = false;
		unitBitmap = null;
	}
	
	@Override
	public void draw(Canvas canvas, int value)
	{
		if (unitBitmap == null)
			return;
		int i = value / framesForCellMy;
		int iPart = value % framesForCellMy;
		unitBitmap.y = (points[i].i * (framesForCellMy - 1 - iPart) + points[i + 1].i * iPart) * GameDraw.A / (framesForCellMy - 1);
		unitBitmap.x = (points[i].j * (framesForCellMy - 1 - iPart) + points[i + 1].j * iPart) * GameDraw.A / (framesForCellMy - 1);
	}
	
	@Override
	public void onEndDraw()
	{
		if (GameHandler.checkCoord(endI, endJ))
			GameDraw.main.gameDrawUnitsDead.keep[endI][endJ] = false;
		GameDraw.main.gameDrawUnits.moveUnits.remove(unitBitmap);
		GameActivity.gameView.thread.needUpdateCampaign = true;
		destroy();
	}
	
}
