package ru.ancientempires.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.ancientempires.Localization;
import ru.ancientempires.action.Checker;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.handler.ActionHelper;
import ru.ancientempires.handler.IGameHandler;

public class Unit extends IGameHandler
{
	
	public UnitType		type;
	public Player		player;
						
	public int			i;
	public int			j;
	public int			health;
	public int			level;
	public int			experience;
	// только для статичных войнов
	public int			numberBuys;
						
	public boolean		isMove;
	public boolean		isTurn;
						
	public Set<Bonus>	bonuses	= new HashSet<Bonus>();
								
	// Локализованное, с учётом уровня
	public String		name;
						
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
			String possibleName = Localization.get(type.name + "." + levelCopy);
			if (possibleName != null)
			{
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
	
	// используется при загрузки игры
	public Unit(Game game)
	{
		setGame(game);
	}
	
	// используется при покупки войнов
	public Unit(UnitType type, Player player, Game game)
	{
		setGame(game);
		setType(type);
		this.player = player;
		initFromType();
	}
	
	public void setType(UnitType type)
	{
		this.type = type;
		// updateName();
	}
	
	public void initFromType()
	{
		health = type.healthDefault;
	}
	
	public Cell getCell()
	{
		return game.fieldCells[i][j];
	}
	
	public Bonus[] getBonuses()
	{
		ArrayList<Bonus> bonuses = new ArrayList<Bonus>(this.bonuses);
		bonuses.addAll(Arrays.asList(type.bonuses));
		return bonuses.toArray(new Bonus[0]);
	}
	
	public int getMoveRadius()
	{
		int moveRadius = type.moveRadius;
		for (Bonus bonus : getBonuses())
			moveRadius += bonus.getBonusMoveStart(game, this);
		return moveRadius;
	}
	
	// TODO кристаллы не должны мочь ходить на горы
	public int getSteps(int i, int j, int targetI, int targetJ)
	{
		return getSteps(game.fieldCells[i][j], game.fieldCells[targetI][targetJ]);
	}
	
	public int getSteps(int i, int j, Cell targetCell)
	{
		return getSteps(game.fieldCells[i][j], targetCell);
	}
	
	public int getSteps(Cell cell, Cell targetCell)
	{
		if (type.isFly)
			return 1;
		int steps = targetCell.getSteps();
		for (Bonus bonus : getBonuses())
			steps += bonus.getBonusMove(game, this, cell, targetCell);
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
		for (Bonus bonus : getBonuses())
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
		for (Bonus bonus : getBonuses())
			defenceBonus += bonus.getBonusDefence(game, this, cell, fromUnit);
		return defenceBonus;
	}
	
	public int getCost()
	{
		int cost = type.cost;
		for (Bonus bonus : getBonuses())
			cost += bonus.getBonusCost(game, this);
		return cost;
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
	
	public void save(DataOutputStream output, Game game) throws IOException
	{
		output.writeUTF(type.name);
		output.writeInt(player.ordinal);
		output.writeInt(i);
		output.writeInt(j);
		output.writeInt(health);
		output.writeInt(level);
		output.writeInt(experience);
		if (type.isStatic)
			output.writeInt(numberBuys);
		output.writeBoolean(isMove);
		output.writeBoolean(isTurn);
		
		output.writeInt(bonuses.size());
		for (Bonus bonus : bonuses)
		{
			bonus.saveBase(output);
			game.numberedBonuses.trySave(output, bonus);
		}
		
		game.namedUnits.trySave(output, this);
		game.numberedUnits.trySave(output, this);
	}
	
	public void load(DataInputStream input, Game game) throws Exception
	{
		setType(game.rules.getUnitType(input.readUTF()));
		initFromType();
		player = game.players[input.readInt()];
		i = input.readInt();
		j = input.readInt();
		health = input.readInt();
		level = input.readInt();
		experience = input.readInt();
		if (type.isStatic)
			numberBuys = input.readInt();
		isMove = input.readBoolean();
		isTurn = input.readBoolean();
		
		MyAssert.a(!isTurn || isMove);
		
		int bonusesSize = input.readInt();
		for (int i = 0; i < bonusesSize; i++)
		{
			Bonus bonus = Bonus.loadBase(input, game.rules);
			bonuses.add(bonus);
			game.numberedBonuses.tryLoad(input, bonus);
		}
		
		game.namedUnits.tryLoad(input, this);
		game.numberedUnits.tryLoad(input, this);
	}
	
	@Override
	public boolean equals(Object o)
	{
		Unit unit = (Unit) o;
		if (type != unit.type)
			return false;
		if (player.ordinal != unit.player.ordinal)
			return false;
		if (i != unit.i)
			return false;
		if (j != unit.j)
			return false;
		if (health != unit.health)
			return false;
		if (level != unit.level)
			return false;
		if (experience != unit.experience)
			return false;
		if (!bonuses.equals(unit.bonuses))
			return false;
		if (numberBuys != unit.numberBuys)
			return false;
		if (isMove != unit.isMove)
			return false;
		if (isTurn != unit.isTurn)
			return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return hashCode() + " " + String.format("%s (%d %d) %d %d (%b %b)", type.name, i, j, player.ordinal, health, isMove, isTurn);
	}
	
	// II
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
	
}
