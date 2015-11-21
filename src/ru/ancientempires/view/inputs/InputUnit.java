package ru.ancientempires.view.inputs;

import java.util.ArrayList;
import java.util.Collections;

import ru.ancientempires.Point;
import ru.ancientempires.action.Action;
import ru.ancientempires.action.ActionResult;
import ru.ancientempires.action.ActionType;
import ru.ancientempires.client.Client;
import ru.ancientempires.view.draws.GameDraw;
import ru.ancientempires.view.draws.GameDrawRangeAll;
import ru.ancientempires.view.draws.onframes.GameDrawUnitAttackMain;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;

public class InputUnit
{
	
	public InputPlayer main;
	
	public InputUnit(InputPlayer main)
	{
		this.main = main;
		drawRange = InputBase.gameDraw.gameDrawRangeAll;
	}
	
	private GameDrawRangeAll	drawRange;
	public boolean				isActive	= false;
	
	private boolean[][]	fieldMoveReal;
	private boolean[][]	fieldAttackReal;
	private boolean[][]	fieldRaiseReal;
	
	private int[][]	previousMoveI;
	private int[][]	previousMoveJ;
	
	private int	radius;
	private int	diameter;
	
	private int	startI;
	private int	startJ;
	private int	endI;
	private int	endJ;
	
	public boolean start(int i, int j)
	{
		Action action = new Action(ActionType.GET_UNIT);
		action.setProperty("i", i);
		action.setProperty("j", j);
		ActionResult result = Client.action(action);
		
		boolean canMove = (boolean) result.getProperty("canMove");
		boolean canAttack = (boolean) result.getProperty("canAttack");
		boolean canRaise = (boolean) result.getProperty("canRaise");
		if (!canMove && !canAttack && !canRaise)
			return false;
			
		startI = i;
		startJ = j;
		previousMoveI = (int[][]) result.getProperty("previousMoveI");
		previousMoveJ = (int[][]) result.getProperty("previousMoveJ");
		fieldMoveReal = (boolean[][]) result.getProperty("fieldMoveReal");
		fieldAttackReal = (boolean[][]) result.getProperty("fieldAttackReal");
		fieldRaiseReal = (boolean[][]) result.getProperty("fieldRaiseReal");
		
		diameter = fieldMoveReal.length;
		radius = diameter / 2;
		isActive = true;
		
		drawRange.start(i, j, result);
		return true;
	}
	
	public boolean tap(int endI, int endJ)
	{
		this.endI = endI;
		this.endJ = endJ;
		int relativeI = radius + endI - startI;
		int relativeJ = radius + endJ - startJ;
		boolean isBoundsNormal = relativeI >= 0 && relativeI < diameter && relativeJ >= 0 && relativeJ < diameter;
		
		ArrayList<ActionType> actionTypes = new ArrayList<ActionType>();
		if (isBoundsNormal)
		{
			if (fieldMoveReal[relativeI][relativeJ])
				actionTypes.add(ActionType.ACTION_UNIT_MOVE);
			if (fieldAttackReal[relativeI][relativeJ])
				actionTypes.add(ActionType.ACTION_UNIT_ATTACK);
			if (fieldRaiseReal[relativeI][relativeJ])
				actionTypes.add(ActionType.ACTION_UNIT_RAISE);
		}
		if (actionTypes.isEmpty())
		{
			destroy();
			return false;
		}
		
		if (actionTypes.size() == 1)
			performAction(actionTypes.get(0));
		else if (actionTypes.size() == 2)
			InputBase.gameDraw.gameDrawAction.start(actionTypes.get(0), actionTypes.get(1));
		return true;
	}
	
	public void performAction(ActionType actionType)
	{
		Action action = new Action(actionType);
		action.setProperty("i", startI);
		action.setProperty("j", startJ);
		action.setProperty("targetI", endI);
		action.setProperty("targetJ", endJ);
		ActionResult result = Client.action(action);
		
		if (actionType == ActionType.ACTION_UNIT_MOVE)
		{
			GameDrawUnitMove gameDraw = new GameDrawUnitMove();
			gameDraw.init(startI, startJ);
			gameDraw.start(InputUnit.getPoints(startI, startJ, endI, endJ, previousMoveI, previousMoveJ), result);
			GameDraw.main.gameDrawPlayer.add(gameDraw);
			destroy();
		}
		else if (actionType == ActionType.ACTION_UNIT_ATTACK)
		{
			main.tapWithoutAction(endI, endJ);
			if ((boolean) result.getProperty("isAttackUnit"))
				GameDraw.main.gameDrawPlayer.add(new GameDrawUnitAttackMain().start(result));
			else
			{
				InputBase.gameDraw.gameDrawBuildingSmokes.update();
				InputBase.gameDraw.gameDrawCellAttack.start(result, endI, endJ);
			}
			destroy();
		}
		else
		{
			InputBase.gameDraw.gameDrawInfo.update();
			InputBase.gameDraw.gameDrawUnitRaise.start(endI, endJ);
			destroy();
		}
	}
	
	public static Point[] getPoints(int startI, int startJ, int endI, int endJ, int[][] previousMoveI, int[][] previousMoveJ)
	{
		int radius = previousMoveI.length / 2;
		int relToAbsI = -radius + startI;
		int relToAbsJ = -radius + startJ;
		
		ArrayList<Point> points = new ArrayList<Point>();
		Point p = new Point(endI - relToAbsI, endJ - relToAbsJ);
		while (!p.equals(Point.NULL_POINT))
		{
			points.add(new Point(p.i + relToAbsI, p.j + relToAbsJ));
			p = new Point(previousMoveI[p.i][p.j], previousMoveJ[p.i][p.j]);
		}
		Collections.reverse(points);
		return points.toArray(new Point[0]);
	}
	
	public void destroy()
	{
		isActive = false;
		drawRange.end();
	}
	
}
