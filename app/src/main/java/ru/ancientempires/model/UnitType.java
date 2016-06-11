package ru.ancientempires.model;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusCreator;
import ru.ancientempires.serializable.LoaderInfo;
import ru.ancientempires.serializable.Named;
import ru.ancientempires.serializable.Numbered;

public class UnitType implements Named, Numbered
{

	public static UnitType newInstance(String name, LoaderInfo info)
	{
		return info.rules.getUnitType(name);
	}

	public static UnitType newInstance(int i, LoaderInfo info)
	{
		return info.rules.unitTypes[i];
	}

	/*
		KING --- это особый тип война, буквально обозначающий "стандартный король данного игрока".
		То есть при попытке создать война типа KING у игрока синего цвета мы должны получить война типа KING_GALAMAR
		Если нет "специализации" KING для какого-то цвета то замена то используется KING
	*/
	
	public int ordinal;

	public UnitType baseType;
	public String   name;

	public UnitType                   templateType;
	public HashMap<MyColor, UnitType> specializations;

	public void addSpecialization(MyColor color, UnitType specialization)
	{
		if (specializations == null)
			specializations = new HashMap<>();
		specializations.put(color, specialization);
		specialization.templateType = this;
	}
	
	public UnitType trySpecialize(Player player)
	{
		UnitType specialization = specializations == null ? null : specializations.get(player.color);
		return specialization == null ? this : specialization;
	}
	
	public int attackMin;
	public int attackMax;
	public int defence;
	public int moveRadius;
	public int cost;

	public CellType[] repairTypes;
	public CellType[] captureTypes;
	public CellType[] destroyingTypes;

	public Range attackRange;
	public Range attackRangeReverse;

	public Range    raiseRange;
	public UnitType raiseType;

	// Войн не может быть (в текущей реализации) одновременно статичным и с надгробием,
	// иначе при сохранении игры этот войн будет записан в units.dat два раза ---
	// как unitsStaticDead и как fieldUnitsDead, соответственно при загрузке
	// возникнут два разных экземпляра одного и того же война, которые будут конфликтовать.
	public boolean isStatic;
	public boolean hasTombstone;
	public boolean canDoTwoActionAfterOne;
	public boolean isFly;
							
	/*
		Бонусы:
			1. При атаке другого война
				- бонусы от типа
				- бонусы на местности
			2. При получения поля для хода
				а. В начале изменение общего moveRadius
				б. При каждом перемещении между клеточками
	*/
	
	public Bonus[]        bonuses;
	public BonusCreator[] creators;

	// Эти поля используются только для копирования в война
	public int healthDefault;

	public UnitType(String name, int ordinal)
	{
		this.name = name.intern();
		this.ordinal = ordinal;
	}
	
	public UnitType setProperties(UnitType type)
	{
		baseType = type;
		
		healthDefault = type.healthDefault;
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
		raiseType = type.raiseType;
		
		isStatic = type.isStatic;
		hasTombstone = type.hasTombstone;
		canDoTwoActionAfterOne = type.canDoTwoActionAfterOne;
		isFly = type.isFly;
		
		bonuses = type.bonuses;
		creators = type.creators;
		return this;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public int getNumber()
	{
		return ordinal;
	}

	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnitType other = (UnitType) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
