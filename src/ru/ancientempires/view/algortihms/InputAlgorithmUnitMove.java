package ru.ancientempires.view.algortihms;

import java.util.ArrayList;
import java.util.Collections;

import ru.ancientempires.action.ActionType;
import ru.ancientempires.helpers.Point;

public class InputAlgorithmUnitMove extends InputAlgorithmUnitRange
{
	
	private Point[]	ways;
	public int[][]	fieldPrevI;
	public int[][]	fieldPrevJ;
	
	public InputAlgorithmUnitMove(InputAlgoritmMain main)
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
		// this.main.gameDraw.gameDrawCursorDefault.isDrawing = false;
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
				// this.main.gameDraw.gameDrawCursorDefault.isDrawing = true;
				this.main.gameDraw.gameDrawCursorMove.isDrawing = true;
				this.main.gameDraw.gameDrawUnitMove.init(this.startI, this.startJ);
			}
			updateWayLine(i, j);
			this.main.gameDraw.gameDrawWayLine.update(this.ways);
		}
		return isAction;
	}
	
	private void updateWayLine(int absI, int absJ)
	{
		int relToAbsI = -this.radius + this.startI;
		int relToAbsJ = -this.radius + this.startJ;
		
		ArrayList<Point> ways = new ArrayList<Point>();
		Point nullPoint = new Point(-1, -1);
		Point p = new Point(absI - relToAbsI, absJ - relToAbsJ);
		while (!p.equals(nullPoint))
		{
			ways.add(new Point(p.i + relToAbsI, p.j + relToAbsJ));
			p = new Point(this.fieldPrevI[p.i][p.j], this.fieldPrevJ[p.i][p.j]);
		}
		Collections.reverse(ways);
		this.ways = ways.toArray(new Point[0]);
	}
	
	@Override
	public void destroy()
	{
		super.destroy();
		this.main.gameDraw.gameDrawCursors.remove(this.main.gameDraw.gameDrawCursorMove);
		this.main.gameDraw.gameDrawCursorDefault.isDrawing = true;
		this.main.gameDraw.gameDrawWayLine.destroy();
	}
	
	@Override
	public void end()
	{
		super.end();
		
		if (this.isChanged)
			this.main.gameDraw.gameDrawUnitMove.start(this.ways);
	}
	
}
