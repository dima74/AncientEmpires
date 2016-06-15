package ru.ancientempires.helpers;

import java.util.ArrayList;
import java.util.List;

import ru.ancientempires.actions.Checker;
import ru.ancientempires.actions.CheckerCell;
import ru.ancientempires.actions.CheckerUnit;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.AbstractGameHandler;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Unit;

public class ActionHelper extends AbstractGameHandler
{
	
	public ActionHelper(Game game)
	{
		setGame(game);
	}
	
	private boolean isUnitActive(Unit unit)
	{
		return unit != null && !unit.isTurn && unit.player == game.currentPlayer && game.checkFloating(unit);
	}
	
	public boolean canUnitAction(Unit unit)
	{
		return isUnitActive(unit)
		       && (canUnitMove(unit) || game.checkFloating() && (canUnitAttack(unit)
		                                                         || canUnitRepair(unit)
		                                                         || canUnitCapture(unit)
		                                                         || canUnitRaise(unit)));
	}
	
	public boolean canUnitMove(Unit unit)
	{
		return !unit.isMove && isEmptyCells(unit.i, unit.j, unit);
	}
	
	public boolean canUnitAttack(final Unit unit)
	{
		return forCellInRange(unit.i, unit.j, unit.type.attackRange, new CheckerCell()
		{
			@Override
			public boolean check(Cell targetCell)
			{
				Unit targetUnit = game.fieldUnits[targetCell.i][targetCell.j];
				return targetUnit != null && targetUnit.player.team != unit.player.team
				       || targetUnit == null && targetCell.getTeam() != unit.player.team && unit.canDestroy(targetCell.type);
			}
		});
	}
	
	public boolean canUnitRepair(Unit unit)
	{
		Cell cell = unit.getCell();
		return unit.canRepair(cell.type);
	}
	
	public boolean canUnitCapture(Unit unit)
	{
		Cell cell = unit.getCell();
		return unit.canCapture(cell.type) && cell.getTeam() != unit.player.team;
	}
	
	private <T> boolean forEachInRange(T[][] field, int i, int j, Range range, Checker<T> checker)
	{
		int minRelativeI = Math.max(-i, -range.radius);
		int minRelativeJ = Math.max(-j, -range.radius);
		int maxRelativeI = Math.min(game.h - i - 1, range.radius);
		int maxRelativeJ = Math.min(game.w - j - 1, range.radius);
		for (int relativeI = minRelativeI; relativeI <= maxRelativeI; relativeI++)
			for (int relativeJ = minRelativeJ; relativeJ <= maxRelativeJ; relativeJ++)
				if (range.table[range.radius + relativeI][range.radius + relativeJ]
				    && checker.check(field[i + relativeI][j + relativeJ]))
					return true;
		return false;
	}
	
	public <T> List<T> getInRange(T[][] field, int i, int j, Range range, final Checker<T> checker)
	{
		final ArrayList<T> list = new ArrayList<T>();
		forEachInRange(field, i, j, range, new Checker<T>()
		{
			@Override
			public boolean check(T target)
			{
				if (checker.check(target))
					list.add(target);
				return false;
			}
		});
		return list;
	}
	
	public boolean forUnitInRange(Unit[][] field, int i, int j, Range range, CheckerUnit checker)
	{
		return forEachInRange(field, i, j, range, checker);
	}
	
	public boolean forUnitInRange(int i, int j, Range range, CheckerUnit checker)
	{
		return forEachInRange(game.fieldUnits, i, j, range, checker);
	}
	
	public boolean forCellInRange(int i, int j, Range range, CheckerCell checker)
	{
		return forEachInRange(game.fieldCells, i, j, range, checker);
	}
	
	private boolean canUnitRaise(Unit unit)
	{
		return forUnitInRange(game.fieldUnitsDead, unit.i, unit.j, unit.type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit target)
			{
				return target != null;
			}
		});
	}
	
	public boolean isEmptyCells(int i, int j, Unit unit)
	{
		return dfs(i, j, unit, unit.getMoveRadius());
	}
	
	private boolean dfs(int i, int j, Unit unit, int moveRadius)
	{
		return moveRadius >= 0 && (game.fieldUnits[i][j] == null
		                           || tryDfs(i, j, i - 1, j, unit, moveRadius)
		                           || tryDfs(i, j, i, j - 1, unit, moveRadius)
		                           || tryDfs(i, j, i, j + 1, unit, moveRadius)
		                           || tryDfs(i, j, i + 1, j, unit, moveRadius));
	}
	
	private boolean tryDfs(int i, int j, int targetI, int targetJ, Unit unit, int moveRadius)
	{
		return game.checkCoordinates(targetI, targetJ) && dfs(targetI, targetJ, unit, moveRadius - unit.getSteps(i, j, targetI, targetJ));
	}
	
	public void clearUnitState(Unit unit)
	{
		unit.isMove = false;
		unit.isTurn = false;
	}
	
	public Unit[] getUnitsChangedStateNearCell(final Player player, final int targetI, final int targetJ)
	{
		return getInRange(game.fieldUnits, targetI, targetJ, Client.client.rules.rangeMax, new Checker<Unit>()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit != null && !targetUnit.isTurn && targetUnit.player == game.currentPlayer && !canUnitAction(targetUnit);
			}
		}).toArray(new Unit[0]);
	}
	
	// Для вызывания из gameDraw
	public boolean canBuyOnCell(int i, int j)
	{
		return game.checkFloating() && game.fieldCells[i][j].type.buyTypes.length > 0 && game.fieldCells[i][j].player == game.currentPlayer;
	}
	
	public boolean isUnitActive(int i, int j)
	{
		return isUnitActive(game.fieldUnits[i][j]);
	}
	
	public boolean canUnitRepair(int i, int j)
	{
		return isUnitActive(i, j) && game.checkFloating() && canUnitRepair(game.fieldUnits[i][j]);
	}
	
	public boolean canUnitCapture(int i, int j)
	{
		return isUnitActive(i, j) && game.checkFloating() && canUnitCapture(game.fieldUnits[i][j]);
	}
	
}
