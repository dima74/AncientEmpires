package ru.ancientempires.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.ii.II;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.save.GameSaver;
import ru.ancientempires.tasks.NumberedUnits;
import ru.ancientempires.tasks.Task;

// Этот класс описывает снимок игры, кроме поля path, которое относится к сохранению
public class Game
{
	
	public GamePath		path;
	public GameSaver	saver;
	
	// не сохраняется
	public int[]			currentEarns;
	public II				ii;
	public NamedUnits		namedUnits		= new NamedUnits();
	public NumberedUnits	numberedUnits	= new NumberedUnits();
	
	public Random random = new Random();
	
	public Team[]	teams;
	public Player[]	players;
	public Player	currentPlayer;
	
	public Campaign				campaign	= new Campaign(this);
	public int					h;
	public int					w;
	public int					unitsLimit;
	public Cell[][]				fieldCells;
	public Unit[][]				fieldUnits;
	public Set<Unit>			unitsOutside;
	public Unit[][]				fieldUnitsDead;
	public ArrayList<Unit>[]	unitsStaticDead;
	public Unit					floatingUnit;
	
	public int									currentTurn;
	public HashMap<Integer, ArrayList<Task>>	tasks	= new HashMap<Integer, ArrayList<Task>>();
	
	public Player getPlayer(MyColor color)
	{
		for (Player player : players)
			if (player.color == color)
				return player;
		MyAssert.a(false);
		return null;
	}
	
	public void registerTask(Task task)
	{
		if (tasks.get(task.turnToRun) == null)
			tasks.put(task.turnToRun, new ArrayList<Task>());
		tasks.get(task.turnToRun).add(task);
	}
	
	public void runTasks()
	{
		ArrayList<Task> currentTasks = tasks.get(currentTurn);
		if (currentTasks != null)
			for (Task task : currentTasks)
				task.run();
		tasks.remove(currentTurn);
	}
	
	// Из GameHandler
	public boolean checkCoordinates(int i, int j)
	{
		return i >= 0 && i < h && j >= 0 && j < w;
	}
	
	public Unit getUnit(int i, int j)
	{
		if (checkCoordinates(i, j))
			return fieldUnits[i][j];
		return getUnitOutside(i, j);
	}
	
	public void setUnit(int i, int j, Unit unit)
	{
		Unit floatingUnit = getUnit(i, j);
		if (floatingUnit != null)
		{
			MyAssert.a(floatingUnit == null);
			this.floatingUnit = floatingUnit;
		}
		
		unit.i = i;
		unit.j = j;
		if (checkCoordinates(i, j))
			fieldUnits[i][j] = unit;
		else
			unitsOutside.add(unit);
	}
	
	public void removeUnit(int i, int j)
	{
		Unit unit = getUnit(i, j);
		if (checkCoordinates(i, j))
			fieldUnits[i][j] = null;
		else
			unitsOutside.remove(unit);
			
		if (floatingUnit != null && i == floatingUnit.i && j == floatingUnit.j)
		{
			floatingUnit = null;
			setUnit(i, j, floatingUnit);
		}
	}
	
	private Unit getUnitOutside(int i, int j)
	{
		for (Unit unit : unitsOutside)
			if (unit.i == i && unit.j == j)
				return unit;
		return null;
	}
	
	public int amountPlayers()
	{
		return players.length;
	}
	
	public int numberPlayers()
	{
		return players.length;
	}
	
	// TODO кристаллы не должны мочь ходить на горы
	public int getSteps(Unit unit, int nextI, int nextJ)
	{
		if (unit.type.isFly)
			return 1;
		Cell cell = fieldCells[nextI][nextJ];
		int additionalSteps = 0;
		for (BonusOnCellGroup bonusOnCell : unit.type.bonusOnCellWay)
			if (cell.type.group == bonusOnCell.group)
				additionalSteps += bonusOnCell.value;
		return cell.getSteps() + additionalSteps;
	}
	
}
