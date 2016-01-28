package ru.ancientempires.handler;

import java.util.ArrayList;

import ru.ancientempires.action.CheckerCell;
import ru.ancientempires.action.CheckerUnit;
import ru.ancientempires.client.Client;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class ActionHelper extends GameHandler
{
	
	public ActionHelper(Game game)
	{
		setGame(game);
	}
	
	public boolean canUnitAction(Unit unit)
	{
		return unit != null && !unit.isTurn && unit.player == game.currentPlayer
				&& (canUnitMove(unit)
						|| canUnitAttack(unit)
						|| canUnitRepair(unit)
						|| canUnitCapture(unit)
						|| canUnitRaise(unit));
	}
	
	public boolean canUnitMove(Unit unit)
	{
		return !unit.isMove && isEmptyCells(unit.i, unit.j, unit.type);
	}
	
	public boolean canUnitAttack(final Unit unit)
	{
		return forUnitsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit.player.team != unit.player.team;
			}
		}) || forCellsInRange(unit.i, unit.j, unit.type.attackRange, new CheckerCell()
		{
			@Override
			public boolean check(Cell targetCell)
			{
				return unit.type.destroyingTypes[targetCell.type.ordinal] && !targetCell.isDestroy && targetCell.getTeam() != unit.player.team;
			}
		});
	}
	
	public boolean canUnitRepair(Unit unit)
	{
		Cell cell = game.fieldCells[unit.i][unit.j];
		return unit.type.repairTypes[cell.type.ordinal] && cell.isDestroy;
	}
	
	public boolean canUnitCapture(Unit unit)
	{
		Cell cell = game.fieldCells[unit.i][unit.j];
		return unit.type.captureTypes[cell.type.ordinal] && !cell.isDestroy && cell.getTeam() != unit.player.team;
	}
	
	private boolean canUnitRaise(Unit unit)
	{
		return forUnitsInRange(game.fieldUnitsDead, unit.i, unit.j, unit.type.raiseRange, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		});
	}
	
	public boolean forUnitsInRange(int i, int j, Range rangeType, CheckerUnit checker)
	{
		return forUnitsInRange(game.fieldUnits, i, j, rangeType, checker);
	}
	
	public boolean forUnitsInRange(Unit[][] field, int i, int j, Range rangeType, CheckerUnit checker)
	{
		boolean[][] rangeField = rangeType.table;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					return true;
			}
		return false;
	}
	
	public Unit[] getUnitsInRange(int i, int j, Range rangeType, CheckerUnit checker)
	{
		return getUnitsInRange(game.fieldUnits, i, j, rangeType, checker);
	}
	
	public Unit[] getUnitsInRange(Unit[][] field, int i, int j, Range rangeType, CheckerUnit checker)
	{
		ArrayList<Unit> units = new ArrayList<Unit>();
		
		boolean[][] rangeField = rangeType.table;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Unit unit = field[targetI][targetJ];
				if (unit != null && checker.check(unit))
					units.add(unit);
			}
		return units.toArray(new Unit[0]);
	}
	
	public boolean forCellsInRange(int i, int j, Range rangeType, CheckerCell checker)
	{
		boolean[][] rangeField = rangeType.table;
		int size = rangeField.length;
		for (int relI = 0; relI < size; relI++)
			for (int relJ = 0; relJ < size; relJ++)
			{
				if (!rangeField[relI][relJ])
					continue;
				int targetI = i + relI - rangeType.radius;
				int targetJ = j + relJ - rangeType.radius;
				if (!game.checkCoordinates(targetI, targetJ))
					continue;
				Cell cell = game.fieldCells[targetI][targetJ];
				if (cell != null && checker.check(cell))
					return true;
			}
		return false;
	}
	
	public boolean isEmptyCells(int i, int j, UnitType type)
	{
		return true;
	}
	
	public void clearUnitState(Unit unit)
	{
		unit.isMove = false;
		unit.isTurn = false;
	}
	
	public Unit[] getUnitsChangedStateNearCell(final Player player, final int targetI, final int targetJ)
	{
		return getUnitsInRange(targetI, targetJ, Client.client.rules.rangeMax, new CheckerUnit()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				if (targetUnit.player == player && !targetUnit.isTurn && !canUnitAction(targetUnit))
				{
					targetUnit.setTurn();
					return true;
				}
				return false;
			}
		});
	}
	
	// Для вызывания из gameDraw
	
	public boolean canBuyOnCell(int i, int j)
	{
		return game.checkFloating() && !game.fieldCells[i][j].type.buyTypes.isEmpty() && game.fieldCells[i][j].player == game.currentPlayer;
	}
	
	public boolean isUnitActive(int i, int j)
	{
		Unit unit = game.fieldUnits[i][j];
		return unit != null && !unit.isTurn && unit.player == game.currentPlayer && game.checkFloating(unit);
	}
	
	public boolean canUnitRepair(int i, int j)
	{
		return isUnitActive(i, j) && canUnitRepair(game.fieldUnits[i][j]);
	}
	
	public boolean canUnitCapture(int i, int j)
	{
		return isUnitActive(i, j) && canUnitCapture(game.fieldUnits[i][j]);
	}
	
}
