package ru.ancientempires.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import ru.ancientempires.MyColor;
import ru.ancientempires.action.Action;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.NamedUnits;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.ii.II;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.save.GameSaver;
import ru.ancientempires.tasks.Numbered;
import ru.ancientempires.tasks.Task;

// Этот класс описывает снимок игры, кроме поля path, которое относится к сохранению
public class Game
{
	
	// для отладки в ИИ
	public ArrayList<Action>	allActions		= new ArrayList<Action>();
												
	public GamePath				path;
	public GameSaver			saver;
	public boolean				isMain;
	public boolean				isSaver;
								
	// не сохраняется
	public int[]				currentEarns;
	public II					ii				= new II();
	public Rules				rules;
								
	public NamedUnits			namedUnits		= new NamedUnits();
	public Numbered<Unit>		numberedUnits	= new Numbered<>();
	public Numbered<Bonus>		numberedBonuses	= new Numbered<>();
												
	public Random				random			= new Random();
												
	public Game(Rules rules)
	{
		this.rules = rules;
	}
	
	private CellType[] getTypes(String... names)
	{
		CellType[] types = new CellType[names.length];
		for (int i = 0; i < names.length; i++)
			types[i] = rules.getCellType(names[i]);
		return types;
	}
	
	public Game setSize(int h, int w)
	{
		this.h = h;
		this.w = w;
		fieldUnits = new Unit[h][w];
		fieldUnitsDead = new Unit[h][w];
		
		CellType[] types = getTypes("HILL", "TWO_TREES", "THREE_TREES");
		Random random = new Random();
		fieldCells = new Cell[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				fieldCells[i][j] = new Cell(this, types[random.nextInt(types.length)], i, j);
		return this;
	}
	
	public Game setNumberPlayers(int number)
	{
		players = new Player[number];
		for (int i = 0; i < players.length; i++)
			players[i] = new Player(i);
		currentPlayer = players[0];
		
		teams = new Team[number];
		for (int i = 0; i < players.length; i++)
			teams[i] = players[i].team;
			
		unitsStaticDead = new ArrayList[number];
		for (int i = 0; i < unitsStaticDead.length; i++)
			unitsStaticDead[i] = new ArrayList<Unit>();
		return this;
	}
	
	public long getSeed()
	{
		long seed = Game.getSeed(random);
		MyAssert.a(Game.getSeed(new Random(seed)) == seed);
		return Game.getSeed(random);
	}
	
	public static long getSeed(Random random)
	{
		long seed = 0;
		try
		{
			Field field = Random.class.getDeclaredField("seed");
			field.setAccessible(true);
			Number scrambledSeed = (Number) field.get(random); // this needs to be XOR'd with 0x5DEECE66DL
			seed = scrambledSeed.longValue();
		}
		catch (Exception e)
		{
			MyAssert.a(false);
		}
		return seed ^ 0x5DEECE66DL;
	}
	
	public Team[]				teams;
	public Player[]				players;
	public Player				currentPlayer;
								
	public Campaign				campaign		= new Campaign(this);
	public int					h;
	public int					w;
	public int					unitsLimit;
	public Cell[][]				fieldCells;
	public Unit[][]				fieldUnits;
	public Set<Unit>			unitsOutside	= new HashSet<Unit>();
	public Unit[][]				fieldUnitsDead;
	public ArrayList<Unit>[]	unitsStaticDead;
	public int					allowedUnits;
								
	// если на одной клетке стоят два война, то это задний
	public Unit					floatingUnit;
								
	public boolean checkFloating()
	{
		return floatingUnit == null;
	}
	
	public boolean checkFloating(Unit unit)
	{
		return checkFloating() || unit != floatingUnit && unit.i == floatingUnit.i && unit.j == floatingUnit.j;
	}
	
	public int									currentTurn;
	public HashMap<Integer, ArrayList<Task>>	tasks	= new HashMap<Integer, ArrayList<Task>>();
														
	@Override
	public boolean equals(Object obj)
	{
		Game game = (Game) obj;
		
		// System.out.println(get());
		// System.out.println();
		// System.out.println(game.get());
		
		if (!Arrays.deepEquals(teams, game.teams))
		{
			Arrays.deepEquals(teams, game.teams);
			return false;
		}
		if (!Arrays.deepEquals(players, game.players))
		{
			Arrays.deepEquals(players, game.players);
			return false;
		}
		if (currentPlayer.ordinal != game.currentPlayer.ordinal)
			return false;
		if (h != game.h)
			return false;
		if (w != game.w)
			return false;
		if (unitsLimit != game.unitsLimit)
			return false;
		if (!Arrays.deepEquals(fieldCells, game.fieldCells))
			return false;
		if (!Arrays.deepEquals(fieldUnits, game.fieldUnits))
		{
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
				{
					Unit unit = fieldUnits[i][j];
					Unit unit2 = game.fieldUnits[i][j];
					
					if (unit == null && unit2 != null || unit != null && !unit.equals(unit2))
						MyAssert.a(false);
				}
			MyAssert.a(false);
			return false;
		}
		if (!unitsOutside.equals(game.unitsOutside))
			return false;
		if (!Arrays.deepEquals(fieldUnitsDead, game.fieldUnitsDead))
		{
			for (int i = 0; i < h; i++)
				for (int j = 0; j < w; j++)
				{
					Unit unit = fieldUnitsDead[i][j];
					Unit unit2 = game.fieldUnitsDead[i][j];
					if (unit == null && unit2 != null || unit != null && !unit.equals(unit2))
						MyAssert.a(false);
				}
			MyAssert.a(false);
			return false;
		}
		if (!Arrays.deepEquals(unitsStaticDead, game.unitsStaticDead))
			return false;
		if (floatingUnit == null)
		{
			if (game.floatingUnit != null)
				return false;
		}
		else if (!floatingUnit.equals(game.floatingUnit))
			return false;
		if (getSeed() != game.getSeed())
			return false;
		return true;
	}
	
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
			MyAssert.a(this.floatingUnit == null);
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
			setUnit(i, j, floatingUnit);
			floatingUnit = null;
		}
	}
	
	private Unit getUnitOutside(int i, int j)
	{
		for (Unit unit : unitsOutside)
			if (unit.i == i && unit.j == j)
				return unit;
		return null;
	}
	
	public int numberPlayers()
	{
		return players.length;
	}
	
	//
	public String get()
	{
		// int health = fieldUnits[3][7] == null ? 0 : fieldUnits[3][7].health;
		// String s = "king health: " + health + "\n";
		
		String s = "   ";
		for (int i = 0; i < w; i++)
			s += String.format("%3d", i);
		s += "\n";
		
		int ih = 0;
		for (Unit[] line : fieldUnits)
		{
			s += String.format("%2d ", ih++);
			for (Unit unit : line)
			{
				char c;
				if (unit == null)
					c = '.';
				else
				{
					c = unit.type.name.charAt(0);
					if (unit.player.ordinal == 0)
						c = Character.toLowerCase(c);
				}
				s += String.format("%3c", c);
			}
			s += "\n";
		}
		return s;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		if (isMain)
			s += "main";
		if (isSaver)
			s += "save";
		return s + " " + hashCode() + " " + getSeed();
	}
	
}
