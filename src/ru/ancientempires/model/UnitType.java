package ru.ancientempires.model;

import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusCreator;

public class UnitType
{
	
	public UnitType	baseType;
	public String	name;
	
	public int	health;
	public int	attackMin;
	public int	attackMax;
	public int	defence;
	public int	moveRadius;
	public int	cost;
	
	public CellType[]	repairTypes;
	public CellType[]	captureTypes;
	public CellType[]	destroyingTypes;
	
	public Range	attackRange;
	public Range	attackRangeReverse;
	
	public Range	raiseRange;
	public UnitType	raiseUnit;
	
	public boolean	isStatic;
	public boolean	hasTombstone;
	public boolean	canDoTwoActionAfterOne;
	public boolean	isFly;
	
	/*
		Бонусы:
			1. При атаке другого война
				- бонусы от типа
				- бонусы на местности
			2. При получения поля для хода
				а. В начале изменение общего moveRadius
				б. При каждом перемещении между клеточками
	*/
	
	public Bonus[]			bonuses;
	public BonusCreator[]	creators;
	
	public UnitType(String name)
	{
		this.name = name.intern();
	}
	
	public UnitType setProperties(UnitType type)
	{
		baseType = type;
		
		health = type.health;
		attackMin = type.attackMin;
		attackMax = type.attackMax;
		defence = type.defence;
		moveRadius = type.moveRadius;
		cost = type.cost;
		
		repairTypes = type.repairTypes;
		captureTypes = type.captureTypes;
		destroyingTypes = type.destroyingTypes;
		
		attackRange = type.attackRange;
		attackRangeReverse = type.attackRangeReverse;
		raiseRange = type.raiseRange;
		raiseUnit = type.raiseUnit;
		
		isStatic = type.isStatic;
		hasTombstone = type.hasTombstone;
		canDoTwoActionAfterOne = type.canDoTwoActionAfterOne;
		isFly = type.isFly;
		
		bonuses = type.bonuses;
		creators = type.creators;
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
