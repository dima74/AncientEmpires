package ru.ancientempires.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;

import ru.ancientempires.Localization;
import ru.ancientempires.actions.Checker;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.ActionHelper;
import ru.ancientempires.serializable.AfterFromJson;
import ru.ancientempires.serializable.CheckForNullAndEmpty;
import ru.ancientempires.serializable.Exclude;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.OnlyIf;
import ru.ancientempires.serializable.SerializableJson;
import ru.ancientempires.serializable.WithNamed;
import ru.ancientempires.serializable.WithNumbered;

@WithNamed("namedUnits")
@WithNumbered("numberedUnits")
public class Unit extends AbstractGameHandler implements SerializableJson
{

	public UnitType type;
	public Player   player;

	public int i;
	public int j;
	public int health;
	public int level;
	public int experience;
	// только для статичных войнов
	@OnlyIf("isStatic")
	public int numberBuys;

	private boolean isStatic()
	{
		return type.isStatic;
	}

	public boolean isMove;
	public boolean isTurn;

	@CheckForNullAndEmpty
	private ArrayList<Bonus> bonuses;
	@Exclude
	private Bonus[] bonusesCache = new Bonus[0];

	public void removeBonus(Bonus bonus)
	{
		if (bonuses == null)
			bonuses = new ArrayList<>();
		MyAssert.a(bonuses.remove(bonus));
		createBonusesCache();
	}

	public void addBonus(Bonus bonus)
	{
		if (bonuses == null)
			bonuses = new ArrayList<>();
		bonuses.add(bonus);
		createBonusesCache();
	}

	@AfterFromJson
	private void createBonusesCache()
	{
		if (bonuses == null)
		{
			bonusesCache = type.bonuses;
			return;
		}
		bonusesCache = new Bonus[type.bonuses.length + bonuses.size()];
		for (int i = 0; i < type.bonuses.length; i++)
			bonusesCache[i] = type.bonuses[i];
		for (int i = 0; i < bonuses.size(); i++)
			bonusesCache[type.bonuses.length + i] = bonuses.get(i);
	}

	// Локализованное, с учётом уровня
	@Exclude
	public String name;

	public void setTurn()
	{
		isMove = true;
		isTurn = true;
	}

	public int getQualitySum()
	{
		return type.attackMin + type.attackMax + type.defence;
	}

	public final int getNextRankExperience()
	{
		return getQualitySum() * 100 * 2 / 3;
	}

	public boolean isLevelUp()
	{
		int nextLevelExperience = getNextRankExperience();
		return experience >= nextLevelExperience;
	}

	public boolean levelUp()
	{
		experience -= getNextRankExperience();
		level++;
		return updateName();
	}

	public boolean updateName()
	{
		boolean result = updateName(this, type, level);
		MyAssert.a(name != null);
		return result;
	}

	public static boolean updateName(Unit unit, UnitType type, int level)
	{
		for (int levelCopy = level; levelCopy >= 0; levelCopy--)
		{
			String key = type.name + "." + levelCopy;
			if (Localization.contains(key))
			{
				String possibleName = Localization.get(key);
				boolean result = !possibleName.equals(unit.name);
				unit.name = possibleName;
				return result;
			}
		}
		if (type.baseType == null || type.baseType == type)
			return false;
		return updateName(unit, type.baseType, level);
	}

	public String getDescription()
	{
		return getDescription(type);
	}

	public static String getDescription(UnitType type)
	{
		String description = Localization.get(type.name + ".description");
		if (description != null)
			return description;
		if (type.baseType == null || type.baseType == type)
		{
			MyAssert.a(false);
			return null;
		}
		return getDescription(type.baseType);
	}

	/* Два варианта создания нового объекта
	 1. При загрузке игры:
	 	экземпляр
	 -> свойства из unitType
	 -> загружаем оставшиеся свойства

	 2. При покупки в замке/воскрешении:
	 	экземпляр
	 -> свойства из unitType
	 */

	// LoaderInfo.fromJson() -> newInstance
	public Unit()
	{}

	// используется при покупки войнов
	public Unit(Game game, UnitType type, Player player)
	{
		setGame(game);
		this.type = type;
		this.player = player;
		initFromType();
	}

	public Unit(Unit unit)
	{
		this(unit.game, unit.type, unit.player);
	}

	public void initFromType()
	{
		type = type.trySpecialize(player);
		health = type.healthDefault;
		createBonusesCache();
	}

	public Unit setIJ(int i, int j)
	{
		this.i = i;
		this.j = j;
		return this;
	}

	public Unit addToGame()
	{
		game.setUnit(i, j, this);
		player.units.add(this);
		return this;
	}

	public Unit setLevel(int level)
	{
		this.level = level;
		return this;
	}

	public Cell getCell()
	{
		return game.fieldCells[i][j];
	}

	/*
	// оказывается этот метод довольно медленный
	public Bonus[] getBonuses()
	{
		ArrayList<Bonus> bonuses = new ArrayList<>(Arrays.asList(type.bonuses));
		if (this.bonuses != null)
			bonuses.addAll(this.bonuses);
		return bonuses.toArray(new Bonus[bonuses.size()]);
	}

	public Bonus[] bonusesCache()
	{
		Bonus[] bonuses = getBonuses();
		for (int i = 0; i < bonuses.length; i++)
			MyAssert.a(bonuses[i] == bonusesCache[i]);
		return bonusesCache;
	}
	//*/

	public int getMoveRadius()
	{
		int moveRadius = type.moveRadius;
		for (Bonus bonus : bonusesCache)
			moveRadius += bonus.getBonusMoveStart(game, this);
		return moveRadius;
	}

	public int getSteps(int targetI, int targetJ)
	{
		return getSteps(game.fieldCells[targetI][targetJ]);
	}

	public int getSteps(Cell targetCell)
	{
		if (type.isFly)
			return 1;
		int steps = targetCell.getSteps();
		for (Bonus bonus : bonusesCache)
			steps += bonus.getBonusMove(game, this, targetCell);
		return steps;
	}

	public int getBonusAttack(Unit targetUnit)
	{
		return getBonusAttack(getCell(), targetUnit);
	}

	public int getBonusAttack(int i, int j, Unit targetUnit)
	{
		return getBonusAttack(game.fieldCells[i][j], targetUnit);
	}

	public int getBonusAttack(Cell cell, Unit targetUnit)
	{
		int attackBonus = 0;
		for (Bonus bonus : bonusesCache)
			attackBonus += bonus.getBonusAttack(game, this, cell, targetUnit);
		return attackBonus;
	}

	public int getBonusDefence(Unit fromUnit)
	{
		return getBonusDefence(getCell(), fromUnit);
	}

	public int getBonusDefence(int i, int j, Unit fromUnit)
	{
		return getBonusDefence(game.fieldCells[i][j], fromUnit);
	}

	public int getBonusDefence(Cell cell, Unit fromUnit)
	{
		int defenceBonus = cell.type.defense;
		for (Bonus bonus : bonusesCache)
			defenceBonus += bonus.getBonusDefence(game, this, cell, fromUnit);
		return defenceBonus;
	}

	public int getCost()
	{
		int cost = type.cost;
		for (Bonus bonus : bonusesCache)
			cost += bonus.getBonusCost(game, this);
		return cost;
	}

	public boolean hasPositiveBonus()
	{
		if (bonuses != null)
			for (Bonus bonus : bonuses)
				if (bonus.getSign() > 0)
					return true;
		return false;
	}

	public boolean hasNegativeBonus()
	{
		if (bonuses != null)
			for (Bonus bonus : bonuses)
				if (bonus.getSign() < 0)
					return true;
		return false;
	}

	// Пожалуй, эти две функции - единственное место,
	// которое делается не за логарифм, хотя можно было бы соптимайзить)))
	public boolean canCapture(CellType cellType)
	{
		return Arrays.asList(type.captureTypes).contains(cellType);
	}

	public boolean canRepair(CellType cellType)
	{
		return Arrays.asList(type.repairTypes).contains(cellType);
	}

	public boolean canDestroy(CellType cellType)
	{
		return Arrays.asList(type.destroyingTypes).contains(cellType);
	}

	@Override
	public int hashCode()
	{
		return 31 * i + j;
	}

	@Override
	public String toString()
	{
		return String.format("%15s (%2d %2d) %d %d (%b %b)", type.name, i, j, player.ordinal, health, isMove, isTurn);
	}

	// II
	@Exclude
	public int var_8f7;

	public int sub_462(Unit targetUnit)
	{
		return this.sub_462(j, i, targetUnit);
	}

	// некоторая характеристика того состояния, которое будет если этот войн сходит на клеточку (i, j) и атакует targetUnit
	public int sub_462(int j, int i, Unit targetUnit)
	{
		// System.out.println(attack * 2 + " " + defence + " " + getOffenceBonusAgainstUnitEx(targetUnit, j, i) + " " + getDefenceBonusAgainstUnitEx(targetUnit, j, i));
		// return (type.attackMin + type.attackMax + getOffenceBonusAgainstUnitEx(targetUnit, j, i)
		// + type.defence + getDefenceBonusAgainstUnitEx(targetUnit, j, i)) * health / 100;
		return (type.attackMin + type.attackMax + getBonusAttack(i, j, targetUnit)
				+ type.defence + getBonusDefence(i, j, targetUnit)) * health / 100;
	}

	public Unit[] getUnitsWithinRange(int x, int y, int minRange, int maxRange, byte b)
	{
		Range type = new Range(null, minRange, maxRange);
		return new ActionHelper(game).getInRange(game.fieldUnits, y, x, type, new Checker<Unit>()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit != null && player.team != targetUnit.player.team;
			}
		}).toArray(new Unit[0]);
	}

	public Unit[] getUnitsToAttack(int x, int y)
	{
		return new ActionHelper(game).getInRange(game.fieldUnits, y, x, type.attackRange, new Checker<Unit>()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return targetUnit != null && player.team != targetUnit.player.team;
			}
		}).toArray(new Unit[0]);
	}

	public Unit[] getUnitsToRaise(int x, int y)
	{
		return new ActionHelper(game).getInRange(game.fieldUnitsDead, y, x, type.raiseRange, new Checker<Unit>()
		{
			@Override
			public boolean check(Unit targetUnit)
			{
				return true;
			}
		}).toArray(new Unit[0]);
	}

	// =/({||})\=
	// from spoon

	public JsonObject toJson()
	{
		JsonObject object = new JsonObject();
		object.addProperty("type", type.getName());
		object.addProperty("player", player.getNumber());
		object.addProperty("i", i);
		object.addProperty("j", j);
		object.addProperty("health", health);
		object.addProperty("level", level);
		object.addProperty("experience", experience);
		if (isStatic())
			object.addProperty("numberBuys", numberBuys);
		object.addProperty("isMove", isMove);
		object.addProperty("isTurn", isTurn);
		if (bonuses != null && !bonuses.isEmpty())
			object.add("bonuses", ru.ancientempires.serializable.SerializableJsonHelper.toJsonArray(bonuses));
		game.namedUnits.toJsonPart(object, this);
		game.numberedUnits.toJsonPart(object, this);
		return object;
	}

	public Unit fromJson(JsonObject object, LoaderInfo info) throws Exception
	{
		game = info.game;
		type = UnitType.newInstance(object.get("type").getAsString(), info);
		player = Player.newInstance(object.get("player").getAsInt(), info);
		i = object.get("i").getAsInt();
		j = object.get("j").getAsInt();
		health = object.get("health").getAsInt();
		level = object.get("level").getAsInt();
		experience = object.get("experience").getAsInt();
		if (isStatic())
			numberBuys = object.get("numberBuys").getAsInt();
		isMove = object.get("isMove").getAsBoolean();
		isTurn = object.get("isTurn").getAsBoolean();
		if (object.has("bonuses"))
			bonuses = new ArrayList<>(Arrays.asList(Bonus.fromJsonArray(object.get("bonuses").getAsJsonArray(), info)));
		game.namedUnits.fromJsonPart(object, this);
		game.numberedUnits.fromJsonPart(object, this);
		createBonusesCache();
		return this;
	}

	static public Unit[] fromJsonArray(JsonArray jsonArray, LoaderInfo info) throws Exception
	{
		Unit[] array = new Unit[jsonArray.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = new Unit().fromJson((com.google.gson.JsonObject) jsonArray.get(i), info);
		return array;
	}

}
