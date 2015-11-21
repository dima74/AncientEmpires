package ru.ancientempires.view.inputs;

import java.util.ArrayList;
import java.util.Collections;

import ru.ancientempires.Point;
import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitMove extends InputAlgorithmUnitRange
{
	
	private Point[]	points;
	public int[][]	fieldPrevI;
	public int[][]	fieldPrevJ;
	
	public InputAlgorithmUnitMove(InputPlayer main)
	{
		super(main);
		// this.gameDrawZone = main.gameDraw.gameDrawZoneMove;
		actionType = ActionType.ACTION_UNIT_MOVE;
		actionTypeGet = ActionType.GET_UNIT_WAY;
	}
	
	@Override
	public void start(int i, int j)
	{
		super.start(i, j);
		fieldPrevI = (int[][]) resultGet.getProperty("prevI");
		fieldPrevJ = (int[][]) resultGet.getProperty("prevJ");
		InputBase.gameDraw.gameDrawCursors.add(InputBase.gameDraw.gameDrawCursorMove);
		InputBase.gameDraw.gameDrawCursorMove.isDrawing = false;
	}
	
	@Override
	public boolean tap(int i, int j)
	{
		boolean isChanged = this.isChanged;
		boolean isAction = super.tap(i, j);
		if (isAction)
		{
			if (!isChanged)
			{
				// InputBase.gameDraw.gameDrawCursorMove.isDrawing = true;
				// InputBase.gameDraw.gameDrawUnitMove.init(startI, startJ);
			}
			updateWayLine(i, j);
			// InputBase.gameDraw.gameDrawWayLine.update(this.points);
		}
		return isAction;
	}
	
	private void updateWayLine(int absI, int absJ)
	{
		points = InputAlgorithmUnitMove.getWayLine(startI, startJ, absI, absJ, fieldPrevI, fieldPrevJ);
	}
	
	public static Point[] getWayLine(int startI, int startJ, int absI, int absJ, int[][] fieldPreviousI, int[][] fieldPreviousJ)
	{
		int radius = fieldPreviousI.length / 2;
		int relToAbsI = -radius + startI;
		int relToAbsJ = -radius + startJ;
		
		ArrayList<Point> points = new ArrayList<Point>();
		Point p = new Point(absI - relToAbsI, absJ - relToAbsJ);
		while (!p.equals(Point.NULL_POINT))
		{
			points.add(new Point(p.i + relToAbsI, p.j + relToAbsJ));
			p = new Point(fieldPreviousI[p.i][p.j], fieldPreviousJ[p.i][p.j]);
		}
		Collections.reverse(points);
		return points.toArray(new Point[0]);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		InputBase.gameDraw.gameDrawCursors.remove(InputBase.gameDraw.gameDrawCursorMove);
		InputBase.gameDraw.gameDrawCursorDefault.isDrawing = true;
		// InputBase.gameDraw.gameDrawWayLine.destroy();
		// if (!isChanged)
		// InputBase.gameDraw.gameDrawUnitMove.destroy();
	}
	
	@Override
	public void end()
	{
		super.end();
		// if (isChanged)
		// InputBase.gameDraw.gameDrawUnitMove.start(points, result);
	}
	
}
