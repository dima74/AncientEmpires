package ru.ancientempires.draws.onframes;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import ru.ancientempires.GameThread;
import ru.ancientempires.Point;
import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.images.bitmaps.UnitBitmap;

public class DrawUnitMove extends DrawOnFramesWithRangeValues
{
	
	public static int	framesForCell	= 8;							// ii-2, player-4
	public int			framesForCellMy	= DrawUnitMove.framesForCell;
	
	public UnitBitmap	unitBitmap;
	public Point[]		points;
	public int			targetI;
	public int			targetJ;
	
	private void initFromEnd()
	{
		unitBitmap = new UnitBitmap(game.getUnit(targetI, targetJ));
	}
	
	private void initFromStart(int i, int j)
	{
		unitBitmap = game.checkCoordinates(i, j)
				? main.units.field[i][j]
				: new UnitBitmap(game.getUnit(i, j));
		unitBitmap.keepTurn = true;
	}
	
	public void start(Point[] points, ActionResultUnitMove result, boolean initFromStart)
	{
		this.points = points;
		targetI = points[points.length - 1].i;
		targetJ = points[points.length - 1].j;
		
		if (initFromStart)
			initFromStart(points[0].i, points[0].j);
		else
			initFromEnd();
			
		if (game.checkCoordinates(targetI, targetJ))
		{
			main.units.field[targetI][targetJ] = unitBitmap;
			main.units.move[targetI][targetJ] = true;
			main.unitsDead.keep[targetI][targetJ] = true;
		}
		
		animateRange(0, (points.length - 1) * framesForCellMy - 1);
		if (result != null)
			main.add(new DrawUnitMoveEnd(result, frameCount));
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
		unitBitmap.y = (points[i].i * (framesForCellMy - 1 - iPart) + points[i + 1].i * iPart) * A / (framesForCellMy - 1);
		unitBitmap.x = (points[i].j * (framesForCellMy - 1 - iPart) + points[i + 1].j * iPart) * A / (framesForCellMy - 1);
		drawUnit(canvas, unitBitmap);
		unitBitmap.move();
	}
	
	public void drawUnit(Canvas canvas, UnitBitmap unitBitmap)
	{
		canvas.drawBitmap(unitBitmap.getBaseBitmap(), unitBitmap.x, unitBitmap.y, null);
		Bitmap healthBitmap = unitBitmap.getHealthBitmap();
		if (healthBitmap != null)
			canvas.drawBitmap(unitBitmap.getHealthBitmap(), unitBitmap.x, unitBitmap.y + A - SmallNumberImages().h, null);
	}
	
	@Override
	public void onEnd()
	{
		if (game.checkCoordinates(targetI, targetJ))
		{
			main.units.move[targetI][targetJ] = false;
			main.unitsDead.keep[targetI][targetJ] = false;
		}
		unitBitmap.hanlers = null;
		GameThread.thread.needUpdateCampaign = true;
		destroy();
	}
	
}
