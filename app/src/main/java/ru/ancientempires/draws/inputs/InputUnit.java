package ru.ancientempires.draws.inputs;

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
import ru.ancientempires.draws.DrawCellAttack;
import ru.ancientempires.draws.DrawRange;
import ru.ancientempires.draws.onframes.DrawUnitAttackMain;
import ru.ancientempires.draws.onframes.DrawUnitMove;
import ru.ancientempires.draws.onframes.DrawUnitRaise;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.ActionHelper;

public class InputUnit extends AbstractInput
{
	
	public InputPlayer inputPlayer;
	
	public InputUnit(InputPlayer inputPlayer)
	{
		this.inputPlayer = inputPlayer;
	}
	
	public boolean isActive = false;

	private ActionResultGetUnit result;

	private int radius;
	private int diameter;

	private int i;
	private int j;
	private int targetI;
	private int targetJ;

	private DrawRange drawRange;

	public boolean isSameTap(int i, int j)
	{
		return this.i == i && this.j == j;
	}
	
	public boolean start(int i, int j)
	{
		if (!new ActionHelper(game).isUnitActive(i, j))
			return false;
		result = (ActionResultGetUnit) new ActionGetUnit()
				.setIJ(i, j)
				.perform(game);
		if (!result.canMove && !result.canAttack && !result.canRaise)
			return false;

		this.i = i;
		this.j = j;
		
		diameter = result.fieldMoveReal.length;
		radius = diameter / 2;
		isActive = true;
		
		drawRange = new DrawRange(i, j, result);
		drawMain.add(drawRange);
		return true;
	}
	
	public boolean tap(int targetI, int targetJ)
	{
		this.targetI = targetI;
		this.targetJ = targetJ;
		int relativeI = radius + targetI - i;
		int relativeJ = radius + targetJ - j;
		boolean isBoundsNormal = relativeI >= 0 && relativeI < diameter && relativeJ >= 0 && relativeJ < diameter;
		
		ArrayList<ActionFromTo> actions = new ArrayList<ActionFromTo>();
		if (isBoundsNormal)
		{
			if (result.fieldMoveReal[relativeI][relativeJ])
				actions.add(new ActionUnitMove());
			if (result.fieldAttackReal[relativeI][relativeJ])
				actions.add(new ActionUnitAttack());
			if (result.fieldRaiseReal[relativeI][relativeJ])
				actions.add(new ActionUnitRaise());
		}
		if (actions.isEmpty())
		{
			destroy();
			return false;
		}
		
		if (actions.size() == 1)
			performAction(actions.get(0));
		else if (actions.size() == 2)
			drawMain.action.start(actions.get(0), actions.get(1));
		return true;
	}
	
	public void performAction(ActionFromTo action)
	{
		action.setIJ(i, j);
		action.setTargetIJ(targetI, targetJ);
		
		if (action.getClass() == ActionUnitMove.class)
		{
			ActionResultUnitMove result = ((ActionUnitMove) action).perform(game);
			DrawUnitMove draw = new DrawUnitMove();
			draw.start(InputUnit.getPoints(i, j, targetI, targetJ, this.result.previousMoveI, this.result.previousMoveJ), result, true);
			drawMain.add(draw);
			destroy();
		}
		else if (action.getClass() == ActionUnitAttack.class)
		{
			ActionResultUnitAttack result = ((ActionUnitAttack) action).perform(game);
			inputPlayer.tapWithoutAction(targetI, targetJ);
			if (result.isAttackUnit)
				drawMain.add(new DrawUnitAttackMain().start(result));
			else
			{
				drawMain.buildingSmokes.update();
				drawMain.add(new DrawCellAttack(result, targetI, targetJ));
			}
			destroy();
		}
		else if (action.getClass() == ActionUnitRaise.class)
		{
			action.perform(game);
			drawMain.info.update();
			drawMain.add(new DrawUnitRaise(targetI, targetJ));
			destroy();
		}
		else
			MyAssert.a(false);
	}
	
	public static Point[] getPoints(int startI, int startJ, int targetI, int targetJ, int[][] previousMoveI, int[][] previousMoveJ)
	{
		int radius = previousMoveI.length / 2;
		int relativeToAbsoluteI = -radius + startI;
		int relativeToAbsoluteJ = -radius + startJ;
		
		ArrayList<Point> points = new ArrayList<Point>();
		Point p = new Point(targetI - relativeToAbsoluteI, targetJ - relativeToAbsoluteJ);
		while (!p.equals(Point.NULL_POINT))
		{
			points.add(new Point(p.i + relativeToAbsoluteI, p.j + relativeToAbsoluteJ));
			p = new Point(previousMoveI[p.i][p.j], previousMoveJ[p.i][p.j]);
		}
		Collections.reverse(points);
		return points.toArray(new Point[0]);
	}
	
	public void destroy()
	{
		isActive = false;
		drawMain.remove(drawRange);
	}
	
}
