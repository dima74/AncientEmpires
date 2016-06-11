package ru.ancientempires.draws.onframes;

import android.graphics.Canvas;

import ru.ancientempires.Point;
import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.draws.DrawLevel;
import ru.ancientempires.images.bitmaps.UnitBitmap;

public class DrawUnitMove extends DrawOnFramesWithRangeValues
{
	
	public static int framesForCell   = 8;                            // ii-2, player-4
	public        int framesForCellMy = DrawUnitMove.framesForCell;

	public UnitBitmap unitBitmap;
	public Point[]    points;
	public int        targetI;
	public int        targetJ;
	public boolean makeSmoke = true;

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
	
	public DrawUnitMove start(Point[] points, ActionResultUnitMove result, boolean initFromStart)
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
		
		animateRange(0, (points.length - 1) * framesForCellMy);
		if (result != null)
			main.add(new DrawUnitMoveEnd(result, frameCount));
		return this;
	}
	
	public DrawUnitMove setMakeSmoke(boolean makeSmoke)
	{
		this.makeSmoke = makeSmoke;
		return this;
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
		unitBitmap.y = (points[i].i * (framesForCellMy - 1 - iPart) + points[i + (frameLeft == 0 ? 0 : 1)].i * iPart) * A / (framesForCellMy - 1);
		unitBitmap.x = (points[i].j * (framesForCellMy - 1 - iPart) + points[i + (frameLeft == 0 ? 0 : 1)].j * iPart) * A / (framesForCellMy - 1);
		unitBitmap.draw(canvas, iFrame());
		unitBitmap.move();
		
		if (makeSmoke && value > 0 && (iPart == 0 || iPart == framesForCellMy / 2))
		{
			int ySmoke = (int) unitBitmap.y;
			int xSmoke = (int) unitBitmap.x;
			
			int currentI = iPart == 0 ? i : i + 1;
			int previousI = iPart == 0 ? i - 1 : i;
			if (points[currentI].j == points[previousI].j)
			{
				ySmoke += points[currentI].i < points[previousI].i ? 24 : -SmokeImages().hSmall;
				xSmoke += (24 - SmokeImages().wSmall) / 2;
			}
			else
			{
				ySmoke += 24 - SmokeImages().hSmall;
				xSmoke += points[currentI].j < points[previousI].j ? 24 : -SmokeImages().hSmall;
			}
			
			DrawBitmaps draw = new DrawBitmaps()
					.setYX(ySmoke, xSmoke)
					.setBitmaps(SmokeImages().bitmapsSmall)
					.setFramesForBitmap((main.rnd.nextInt(3) + 1) * 2)
					.animateRepeat(1);
			main.add(draw, DrawLevel.BOTTOM);
		}
	}
	
	@Override
	public void onEnd()
	{
		if (game.checkCoordinates(targetI, targetJ))
		{
			main.units.move[targetI][targetJ] = false;
			main.unitsDead.keep[targetI][targetJ] = false;
		}
		unitBitmap.handlers = null;
		postUpdateCampaign();
		destroy();
	}
	
}
