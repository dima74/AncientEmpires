package ru.ancientempires.view.inputs;

import java.util.ArrayList;
import java.util.Collections;

import ru.ancientempires.Point;
import ru.ancientempires.action.ActionFromTo;
import ru.ancientempires.action.ActionGetUnit;
import ru.ancientempires.action.ActionUnitAttack;
import ru.ancientempires.action.ActionUnitMove;
import ru.ancientempires.action.ActionUnitRaise;
import ru.ancientempires.action.result.ActionResultGetUnit;
import ru.ancientempires.action.result.ActionResultUnitAttack;
import ru.ancientempires.action.result.ActionResultUnitMove;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.view.draws.GameDrawRangeAll;
import ru.ancientempires.view.draws.IInput;
import ru.ancientempires.view.draws.onframes.GameDrawUnitAttackMain;
import ru.ancientempires.view.draws.onframes.GameDrawUnitMove;

public class InputUnit extends IInput
{
	
	public InputPlayer inputMain;
	
	public InputUnit(InputPlayer main)
	{
		inputMain = main;
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
		ActionResultGetUnit result = (ActionResultGetUnit) new ActionGetUnit()
				.setIJ(i, j)
				.perform(game);
				
		boolean canMove = result.canMove;
		boolean canAttack = result.canAttack;
		boolean canRaise = result.canRaise;
		if (!canMove && !canAttack && !canRaise)
			return false;
			
		startI = i;
		startJ = j;
		previousMoveI = result.previousMoveI;
		previousMoveJ = result.previousMoveJ;
		fieldMoveReal = result.fieldMoveReal;
		fieldAttackReal = result.fieldAttackReal;
		fieldRaiseReal = result.fieldRaiseReal;
		
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
		
		ArrayList<ActionFromTo> actionTypes = new ArrayList<ActionFromTo>();
		if (isBoundsNormal)
		{
			if (fieldMoveReal[relativeI][relativeJ])
				actionTypes.add(new ActionUnitMove());
			if (fieldAttackReal[relativeI][relativeJ])
				actionTypes.add(new ActionUnitAttack());
			if (fieldRaiseReal[relativeI][relativeJ])
				actionTypes.add(new ActionUnitRaise());
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
	
	public void performAction(ActionFromTo action)
	{
		action.setIJ(startI, startJ);
		action.setTargetIJ(endI, endJ);
		
		if (action.getClass() == ActionUnitMove.class)
		{
			ActionResultUnitMove result = ((ActionUnitMove) action).perform(game);
			GameDrawUnitMove gameDraw = new GameDrawUnitMove();
			gameDraw.init(startI, startJ);
			gameDraw.start(InputUnit.getPoints(startI, startJ, endI, endJ, previousMoveI, previousMoveJ), result);
			main.gameDrawPlayer.add(gameDraw);
			destroy();
		}
		else if (action.getClass() == ActionUnitAttack.class)
		{
			ActionResultUnitAttack result = ((ActionUnitAttack) action).perform(game);
			inputMain.tapWithoutAction(endI, endJ);
			if (result.isAttackUnit)
				main.gameDrawPlayer.add(new GameDrawUnitAttackMain().start(result));
			else
			{
				InputBase.gameDraw.gameDrawBuildingSmokes.update();
				InputBase.gameDraw.gameDrawCellAttack.start(result, endI, endJ);
			}
			destroy();
		}
		else if (action.getClass() == ActionUnitRaise.class)
		{
			InputBase.gameDraw.gameDrawInfo.update();
			InputBase.gameDraw.gameDrawUnitRaise.start(endI, endJ);
			destroy();
		}
		else
			MyAssert.a(false);
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
