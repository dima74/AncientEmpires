package ru.ancientempires.view.algortihms;

import java.util.ArrayList;
import java.util.Collections;

import ru.ancientempires.Point;
import ru.ancientempires.action.ActionType;

public class InputAlgorithmUnitMove extends InputAlgorithmUnitRange
{
	
	private Point[]	points;
	public int[][]	fieldPrevI;
	public int[][]	fieldPrevJ;
	
	public InputAlgorithmUnitMove(InputAlgorithmMain main)
	{
		super(main);
		this.gameDrawZone = main.gameDraw.gameDrawZoneMove;
		this.actionType = ActionType.ACTION_UNIT_MOVE;
		this.actionTypeGet = ActionType.GET_UNIT_WAY;
	}
	
	@Override
	public void start(int i, int j)
	{
		super.start(i, j);
		this.fieldPrevI = (int[][]) this.resultGet.getProperty("prevI");
		this.fieldPrevJ = (int[][]) this.resultGet.getProperty("prevJ");
		this.main.gameDraw.gameDrawCursors.add(this.main.gameDraw.gameDrawCursorMove);
		this.main.gameDraw.gameDrawCursorMove.isDrawing = false;
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
				this.main.gameDraw.gameDrawCursorMove.isDrawing = true;
				this.main.gameDraw.gameDrawUnitMove.init(this.startI, this.startJ);
			}
			updateWayLine(i, j);
			this.main.gameDraw.gameDrawWayLine.update(this.points);
		}
		return isAction;
	}
	
	private void updateWayLine(int absI, int absJ)
	{
		this.points = InputAlgorithmUnitMove.getWayLine(this.startI, this.startJ, absI, absJ, this.fieldPrevI, this.fieldPrevJ);
	}
	
	public static Point[] getWayLine(int startI, int startJ, int absI, int absJ, int[][] fieldPrevI, int[][] fieldPrevJ)
	{
		int radius = fieldPrevI.length / 2;
		int relToAbsI = -radius + startI;
		int relToAbsJ = -radius + startJ;
		
		ArrayList<Point> points = new ArrayList<Point>();
		Point p = new Point(absI - relToAbsI, absJ - relToAbsJ);
		while (!p.equals(Point.NULL_POINT))
		{
			points.add(new Point(p.i + relToAbsI, p.j + relToAbsJ));
			p = new Point(fieldPrevI[p.i][p.j], fieldPrevJ[p.i][p.j]);
		}
		Collections.reverse(points);
		return points.toArray(new Point[0]);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		this.main.gameDraw.gameDrawCursors.remove(this.main.gameDraw.gameDrawCursorMove);
		this.main.gameDraw.gameDrawCursorDefault.isDrawing = true;
		this.main.gameDraw.gameDrawWayLine.destroy();
		if (!this.isChanged)
			this.main.gameDraw.gameDrawUnitMove.destroy();
	}
	
	@Override
	public void end()
	{
		super.end();
		if (this.isChanged)
			this.main.gameDraw.gameDrawUnitMove.start(this.points, this.result);
	}
	
}
