package ru.ancientempires.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import ru.ancientempires.MyColor;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.framework.MyAssert;
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
	public int[] currentEarns;
	
	public Random random = new Random();
	
	public Team[]	teams;
	public Player[]	players;
	public Player	currentPlayer;
	
	public Campaign				campaign	= new Campaign();;
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
	
	public NamedUnits		namedUnits		= new NamedUnits();
	public NumberedUnits	numberedUnits	= new NumberedUnits();
	
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
	
}
