package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ru.ancientempires.MyColor;
import ru.ancientempires.actions.Action;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.campaign.Campaign;
import ru.ancientempires.campaign.points.AbstractPoint;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.ii.II;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSaver;
import ru.ancientempires.rules.Rules;
import ru.ancientempires.serializable.DontGenerate;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.SerializableJsonHelper;
import ru.ancientempires.tasks.Task;

// Этот класс описывает снимок игры, кроме поля path, которое относится к сохранению
@DontGenerate
public class Game implements SerializableJson
{
	
	// для отладки в ИИ
	public ArrayList<Action> allActions = new ArrayList<>();

	public GamePath  path;
	public GameSaver saver;
	public boolean   isMain;
	public boolean   isSaver;

	// не сохраняется
	public int[] currentEarns;
	public II    ii;
	public Rules rules;

	public NamedObjects<Boolean>       namedBooleans   = new NamedObjects<>();
	public NamedObjects<Unit>          namedUnits      = new NamedObjects<>();
	public NamedObjects<AbstractPoint> namedPoints     = new NamedObjects<>();
	public NumberedObjects<Unit>       numberedUnits   = new NumberedObjects<>();
	public NumberedObjects<Bonus>      numberedBonuses = new NumberedObjects<>();

	public Random random;

	public Game(Rules rules)
	{
		MyAssert.a(rules != null);
		this.rules = rules;
		ii = new II(rules);
	}

	public Game(GamePath path)
	{
		this(path.getRules());
		this.path = path;
	}

	public Game myClone() throws Exception
	{
		Game game = new Game(rules);
		game.path = path;
		game.fromJson(toJson());
		return game;
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
		Random random = new Random(0);
		fieldCells = new Cell[h][w];
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++)
				new Cell(this, types[random.nextInt(types.length)], i, j);
		return this;
	}

	public Game setNumberPlayers(int number)
	{
		players = new Player[number];
		teams = new Team[number];
		for (int i = 0; i < players.length; i++)
		{
			players[i] = new Player();
			players[i].ordinal = i;
			players[i].units = new ArrayList<>();
		}

		unitsStaticDead = new ArrayList[number];
		for (int i = 0; i < unitsStaticDead.length; i++)
			unitsStaticDead[i] = new ArrayList<>();
		return this;
	}

	public void setNumberTeams(int number)
	{
		teams = new Team[number];
		for (int i = 0; i < teams.length; i++)
			teams[i] = new Team(i);
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
	
	public Team[]   teams;
	public Player[] players;
	public Player   currentPlayer;

	public Campaign campaign = new Campaign(this);
	public int      h;
	public int      w;
	//public int      unitsLimit;
	public Cell[][] fieldCells;
	public Unit[][] fieldUnits;
	public HashSet<Unit> unitsOutside = new HashSet<>();
	public Unit[][]          fieldUnitsDead;
	public ArrayList<Unit>[] unitsStaticDead;
	public Integer           allowedUnits;

	// если на одной клетке стоят два война, то это задний
	public Unit floatingUnit;

	// на каждой клетке не более одного юнита
	public boolean checkFloating()
	{
		return floatingUnit == null;
	}

	// юнит может ходить
	public boolean checkFloating(Unit unit)
	{
		return checkFloating() || unit != floatingUnit && unit.i == floatingUnit.i && unit.j == floatingUnit.j;
	}
	
	public Integer currentTurn;
	public HashMap<Integer, ArrayList<Task>> tasks = new HashMap<>();
	public Unit[][][] buyUnits;

	@Override
	public boolean equals(Object obj)
	{
		JsonObject json1 = toJson();
		JsonObject json2 = ((Game) obj).toJson();
		json1.remove("campaignState");
		json2.remove("campaignState");
		return json1.equals(json2);
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

	public boolean canSetUnit(int i, int j)
	{
		return getUnit(i, j) == null || floatingUnit == null;
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
			if (unit.i == i && unit.j == j && unit != floatingUnit)
				return unit;
		return null;
	}
	
	public int numberPlayers()
	{
		return players.length;
	}

	public int numberTeams()
	{
		return teams.length;
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
		return s + " " + path.gameID;
	}

	public String toJsonPretty()
	{
		return SerializableJsonHelper.toJsonPretty(this);
	}

	public JsonObject toJson()
	{
		MyAssert.a(numberedUnits.objects.isEmpty());
		MyAssert.a(numberedBonuses.objects.isEmpty());

		JsonObject object = new JsonObject();
		object.addProperty("h", h);
		object.addProperty("w", w);
		if (currentPlayer != null)
			object.addProperty("currentPlayer", currentPlayer.color.name());
		object.addProperty("currentTurn", currentTurn);
		object.addProperty("allowedUnits", allowedUnits);
		if (random != null)
			object.addProperty("seed", getSeed());

		// tasks
		// задачи должны быть первыми, так как они (возможно) добавят войнов и бонусов в numbered*
		JsonArray tasksArray = new JsonArray();
		for (Map.Entry<Integer, ArrayList<Task>> entry : tasks.entrySet())
		//if (!entry.getValue().isEmpty())
		{
			MyAssert.a(!entry.getValue().isEmpty());
			tasksArray.add(new JsonPrimitive(entry.getKey()));
			tasksArray.add(new JsonPrimitive(entry.getValue().size()));
			for (Task task : entry.getValue())
				tasksArray.add(task.toJson());
		}
		object.add("tasks", tasksArray);

		// named
		object.add("namedBooleans", namedBooleans.toJsonBoolean());
		object.add("namedPoints", namedPoints.toJson());

		// players
		if (players != null)
			object.add("players", SerializableJsonHelper.toJsonArray(players));

		// cells types
		JsonArray mapArray = new JsonArray();
		for (Cell[] line : fieldCells)
		{
			JsonArray lineArray = new JsonArray();
			for (Cell cell : line)
				lineArray.add(cell.type.ordinal);
			mapArray.add(lineArray);
		}
		object.add("map", mapArray);

		// cells
		ArrayList<Cell> cells = new ArrayList<>();
		for (Cell[] line : fieldCells)
			for (Cell cell : line)
				if (cell.needSave())
					cells.add(cell);
		//output.writeInt(cells.size());
		object.add("cells", SerializableJsonHelper.toJsonArray(cells));

		// units
		if (floatingUnit != null)
			object.add("floatingUnit", floatingUnit.toJson());
		object.add("units", SerializableJsonHelper.toJsonArray(convertFieldToList(fieldUnits)));
		object.add("unitsOutside", SerializableJsonHelper.toJsonArray(unitsOutside));
		object.add("unitsDead", SerializableJsonHelper.toJsonArray(convertFieldToList(fieldUnitsDead)));
		JsonArray unitsStaticDeadArray = new JsonArray();
		for (ArrayList<Unit> list : unitsStaticDead)
			unitsStaticDeadArray.add(SerializableJsonHelper.toJsonArray(list));
		object.add("unitsStaticDead", unitsStaticDeadArray);

		if (campaign.scripts != null /* для PlayersConfigureActivity */ && (!path.isBaseGame || !campaign.isDefault))
			object.add("campaignState", campaign.toJsonState());

		numberedUnits.objects.clear();
		numberedBonuses.objects.clear();
		return SerializableJsonHelper.eraseNulls(object);
	}

	public Game fromJson(JsonObject object) throws Exception
	{
		return fromJson(object, getLoaderInfo());
	}

	public Game fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		MyAssert.a(numberedUnits.objects.isEmpty());
		MyAssert.a(numberedBonuses.objects.isEmpty());
		SerializableJsonHelper.insertDefaults(object, rules.defaultGame);

		h = object.get("h").getAsInt();
		w = object.get("w").getAsInt();
		currentTurn = object.get("currentTurn").getAsInt();
		allowedUnits = object.get("allowedUnits").getAsInt();
		if (object.has("seed"))
			random = new Random(object.get("seed").getAsLong());
		if (path.isBaseGame)
			random = new Random();
		//random = new Random(49950377336979L);
		MyAssert.a(random != null);

		namedBooleans.fromJsonBoolean((JsonObject) object.get("namedBooleans"), info);
		namedPoints.fromJson((JsonObject) object.get("namedPoints"), info, AbstractPoint.class);

		// teams
		teams = new Team[path.numberPlayers];
		for (int i = 0; i < teams.length; i++)
			teams[i] = new Team(i);

		// players
		JsonArray playersJson = SerializableJsonHelper.insertDefaults((JsonArray) object.get("players"), rules.getDefaultsPlayers(path.numberPlayers));
		players = Player.fromJsonArray(playersJson, info);
		for (int i = 0; i < players.length; i++)
		{
			players[i].units = new ArrayList<>();
			players[i].ordinal = i;
		}
		currentPlayer = getPlayer(MyColor.valueOf(object.get("currentPlayer").getAsString()));

		// shrink number teams
		int numberTeams = 0;
		for (Player player : players)
			numberTeams = Math.max(player.team.ordinal + 1, numberTeams);
		Team[] teamsOld = this.teams;
		teams = new Team[numberTeams];
		for (int i = 0; i < teams.length; i++)
			teams[i] = teamsOld[i];

		// teams
		for (Team team : this.teams)
		{
			ArrayList<Player> teamsPlayers = new ArrayList<>();
			for (Player player : players)
				if (player.team == team)
					teamsPlayers.add(player);
			team.players = teamsPlayers.toArray(new Player[teamsPlayers.size()]);
		}
		for (int i = 0; i < this.teams.length; i++)
			this.teams[i].ordinal = i;

		// cells types
		JsonArray mapArray = (JsonArray) object.get("map");
		fieldCells = new Cell[h][w];
		for (int i = 0; i < h; i++)
		{
			JsonArray lineArray = (JsonArray) mapArray.get(i);
			for (int j = 0; j < w; j++)
			{
				int type = lineArray.get(j).getAsInt();
				new Cell(this, rules.cellTypes[type], i, j);
			}
		}

		// cells
		JsonArray cellsArray = (JsonArray) object.get("cells");
		for (JsonElement element : cellsArray)
		{
			JsonObject cellObject = (JsonObject) element;
			int i = cellObject.get("i").getAsInt();
			int j = cellObject.get("j").getAsInt();
			fieldCells[i][j].fromJson(cellObject, info);
		}

		for (Cell[] line : fieldCells)
			for (Cell cell : line)
				if (cell.type.template != null)
					cell.type.template.update(cell);

		// units
		if (object.has("floatingUnit"))
		{
			floatingUnit = new Unit().fromJson((JsonObject) object.get("floatingUnit"), info);
			floatingUnit.player.units.add(floatingUnit);
		}
		fieldUnits = fromJsonUnitsField((JsonArray) object.get("units"), info, true);
		unitsOutside = new HashSet<>(fromJsonUnits((JsonArray) object.get("unitsOutside"), info, true));
		fieldUnitsDead = fromJsonUnitsField((JsonArray) object.get("unitsDead"), info, false);
		unitsStaticDead = new ArrayList[players.length];
		JsonArray unitsStaticDeadArray = (JsonArray) object.get("unitsStaticDead");
		for (int i = 0; i < players.length; i++)
			unitsStaticDead[i] = new ArrayList(fromJsonUnits((JsonArray) unitsStaticDeadArray.get(i), info, false));

		// tasks (таски после войнов --- войны добавляют записи в Numbered*)
		JsonArray tasksArray = (JsonArray) object.get("tasks");
		for (int i = 0; i < tasksArray.size(); i++)
		{
			JsonElement element = tasksArray.get(i);
			MyAssert.a(element.isJsonPrimitive());
			//if (element.isJsonPrimitive())
			{
				ArrayList<Task> tasksList = new ArrayList<>();
				tasks.put(element.getAsInt(), tasksList);
				int size = tasksArray.get(++i).getAsInt();
				while (tasksList.size() < size)
					tasksList.add(info.fromJson((JsonObject) tasksArray.get(++i), Task.class));
			}
		}

		campaign.arrayState = (JsonArray) object.get("campaignState");

		//
		buyUnits = new Unit[players.length][rules.cellTypes.length][];
		for (CellType cellType : rules.cellTypes)
		{
			//cellType.buyUnits = new Unit[players.length][];
			if (cellType.buyTypes.length > 0)
				for (int iPlayer = 0; iPlayer < players.length; iPlayer++)
				{
					//cellType.buyUnits[iPlayer] = new Unit[cellType.buyTypes.length];
					buyUnits[iPlayer][cellType.ordinal] = new Unit[cellType.buyTypes.length];
					for (int i = 0; i < cellType.buyTypes.length; i++)
						buyUnits[iPlayer][cellType.ordinal][i] = new Unit(this, cellType.buyTypes[i], players[iPlayer]);
				}
		}

		//
		currentEarns = new int[players.length];
		for (Cell[] line : fieldCells)
			for (Cell cell : line)
				if (cell.player != null)
					currentEarns[cell.player.ordinal] += cell.type.earn;

		numberedUnits.objects.clear();
		numberedBonuses.objects.clear();
		return this;
	}

	private Unit[][] fromJsonUnitsField(JsonArray array, LoaderInfo info, boolean addToPlayer) throws Exception
	{
		List<Unit> units = fromJsonUnits(array, info, addToPlayer);
		Unit[][] field = new Unit[h][w];
		for (Unit unit : units)
			field[unit.i][unit.j] = unit;
		return field;
	}

	private List<Unit> fromJsonUnits(JsonArray array, LoaderInfo info, boolean addToPlayer) throws Exception
	{
		Unit[] units = info.fromJsonArraySimple(array, Unit.class);
		if (addToPlayer)
			for (Unit unit : units)
				unit.player.units.add(unit);
		return Arrays.asList(units);
	}

	public ArrayList<Unit> convertFieldToList(Unit[][] field)
	{
		ArrayList<Unit> units = new ArrayList<>();
		for (Unit[] line : field)
			for (Unit unit : line)
				if (unit != null)
					units.add(unit);
		return units;
	}

	public LoaderInfo getLoaderInfo()
	{
		return new LoaderInfo(this);
	}

	public String diff(Game game)
	{
		return SerializableJsonHelper.diff(this, game);
	}

}
