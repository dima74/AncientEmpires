package ru.ancientempires.load;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import ru.ancientempires.bonuses.BonusForUnit;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.helpers.JsonHelper;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.CellTypeGroup;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.RangeType;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;

public class RulesLoader
{
	
	private FileLoader loader;
	
	public RulesLoader(FileLoader loader)
	{
		this.loader = loader;
	}
	
	public void load() throws IOException
	{
		// Просто имена типов
		preloadCellTypesNames();
		preloadUnitTypesNames();
		
		loadDefaultCell();
		loadCellTypes();
		// loadCellTypesGroups(cellTypeGroupsNameFile);
		loadRangeTypes();
		loadDefaultUnit();
		loadUnitTypes();
	}
	
	private void preloadCellTypesNames() throws IOException
	{
		JsonReader reader = loader.getReader("allCellTypes.json");
		reader.beginObject();
		MyAssert.a("all_cell_types", reader.nextName());
		String[] typeStrings = new Gson().fromJson(reader, String[].class);
		reader.endObject();
		CellType.setCellTypes(typeStrings);
	}
	
	private void preloadUnitTypesNames() throws IOException
	{
		JsonReader reader = loader.getReader("allUnitTypes.json");
		reader.beginObject();
		MyAssert.a("all_unit_types", reader.nextName());
		String[] typeStrings = new Gson().fromJson(reader, String[].class);
		reader.endObject();
		UnitType.setUnitTypes(typeStrings);
	}
	
	private void loadDefaultCell() throws IOException
	{
		JsonReader reader = loader.getReader("defaultCell.json");
		Cell.defaultCell = RulesLoader.nextCell(reader, null);
	}
	
	private void loadCellTypes() throws IOException
	{
		JsonReader reader = loader.getReader("cellTypes.json");
		reader.beginObject();
		
		MyAssert.a("default_cell_type".equals(reader.nextName()));
		CellType defaultType = nextCellType(reader, null, false);
		
		ArrayList<CellTypeGroup> groups = new ArrayList<CellTypeGroup>();
		
		MyAssert.a("cell_types_groups".equals(reader.nextName()));
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
			groups.add(loadCellTypesGroup(reader, defaultType));
		reader.endArray();
		reader.endObject();
		
		reader.close();
		
		CellType.finishInit();
		CellTypeGroup.setCellTypeGroups(groups);
	}
	
	private CellTypeGroup loadCellTypesGroup(JsonReader reader, CellType defaultType)
			throws IOException
	{
		reader.beginObject();
		MyAssert.a("name".equals(reader.nextName()));
		String name = reader.nextString();
		MyAssert.a("default_cell_type".equals(reader.nextName()));
		CellType groupDefaultType = nextCellType(reader, defaultType, false);
		MyAssert.a("cell_types".equals(reader.nextName()));
		
		CellTypeGroup group = new CellTypeGroup(name);
		
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			CellType cellType = nextCellType(reader, groupDefaultType, true);
			cellType.group = group;
		}
		reader.endArray();
		reader.endObject();
		
		return group;
	}
	
	private CellType nextCellType(JsonReader reader, CellType defaultType, boolean hasName) throws IOException
	{
		CellType type;
		reader.beginObject();
		if (hasName)
		{
			String typeName = JsonHelper.readString(reader, "name");
			type = CellType.getType(typeName);
		}
		else
			type = new CellType();
		if (defaultType != null)
			type.setProperties(defaultType);
		while (reader.peek() != JsonToken.END_OBJECT)
		{
			String name = reader.nextName();
			if ("isStatic".equals(name))
				type.isStatic = reader.nextBoolean();
			else if ("baseDifficulte".equals(name))
				type.baseSteps = reader.nextInt();
			else if ("earn".equals(name))
				type.earn = reader.nextInt();
			else if ("defense".equals(name))
				type.defense = reader.nextInt();
			else if ("isCapture".equals(name))
				type.isCapture = reader.nextBoolean();
			else if ("isDestroying".equals(name))
				type.isDestroying = reader.nextBoolean();
			else if ("isHeal".equals(name))
				type.isHeal = reader.nextBoolean();
			else if ("buyUnits".equals(name))
			{
				String[] buyUnitsNames = new Gson().fromJson(reader, String[].class);
				type.buyUnitsDefault = new ArrayList<UnitType>();
				for (String unitName : buyUnitsNames)
					type.buyUnitsDefault.add(UnitType.getType(unitName));
			}
		}
		reader.endObject();
		return type;
	}
	
	private void loadRangeTypes() throws IOException
	{
		JsonReader reader = loader.getReader("rangeTypes.json");
		
		reader.beginObject();
		MyAssert.a("range_types", reader.nextName());
		
		reader.beginArray();
		ArrayList<RangeType> types = new ArrayList<RangeType>();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
			types.add(nextAttackType(reader));
		reader.endArray();
		
		reader.endObject();
		reader.close();
		
		RangeType.setAttackTypes(types);
	}
	
	private RangeType nextAttackType(JsonReader reader) throws IOException
	{
		RangeType type = new RangeType();
		reader.beginObject();
		type.name = JsonHelper.readString(reader, "name").intern();
		MyAssert.a("table", reader.nextName());
		
		reader.beginArray();
		String string = reader.nextString();
		int length = string.length();
		type.radius = length / 2;
		type.field = new boolean[length][length];
		for (int k = 0; k < length; k++)
		{
			for (int i = 0; i < length; i++)
				type.field[k][i] = string.charAt(i) == '1';
			if (k != length - 1)
				string = reader.nextString();
		}
		reader.endArray();
		
		reader.endObject();
		return type;
	}
	
	private void loadUnitTypes() throws IOException
	{
		JsonReader reader = loader.getReader("unitTypes.json");
		
		reader.beginObject();
		
		// Для каждого имени типа дозагружаем свойства
		MyAssert.a("default_unit_type", reader.nextName());
		UnitType defaultType = nextUnitType(reader, null, false);
		MyAssert.a("unit_types", reader.nextName());
		
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
			nextUnitType(reader, defaultType, true);
		reader.endArray();
		
		reader.endObject();
		reader.close();
		
		UnitType.finishInit();
	}
	
	private UnitType nextUnitType(JsonReader reader, UnitType defaultType, boolean hasName)
			throws IOException
	{
		reader.beginObject();
		UnitType type;
		if (hasName)
		{
			String typeName = JsonHelper.readString(reader, "name");
			type = UnitType.getType(typeName).setProperties(defaultType);
		}
		else
			type = new UnitType();
		while (reader.peek() != JsonToken.END_OBJECT)
		{
			String name = reader.nextName();
			if ("baseHealth".equals(name))
				type.baseHealth = reader.nextInt();
			else if ("attack".equals(name))
				type.attack = (float) reader.nextDouble();
			else if ("attackDelta".equals(name))
				type.attackDelta = (float) reader.nextDouble();
			else if ("defence".equals(name))
				type.defence = reader.nextInt();
			else if ("moveRange".equals(name))
				type.moveRadius = reader.nextInt();
			else if ("captureTypes".equals(name))
				type.captureTypes = nextCellsArray(reader);
			else if ("repairTypes".equals(name))
				type.repairTypes = nextCellsArray(reader);
			else if ("attackType".equals(name))
				type.attackRange = RangeType.getType(reader.nextString());
			else if ("destroyingTypes".equals(name))
				type.destroyingTypes = nextCellsArray(reader);
			else if ("attackTypeReverse".equals(name))
				type.attackRangeReverse = RangeType.getType(reader.nextString());
			else if ("raiseRangeType".equals(name))
				type.raiseRange = RangeType.getType(reader.nextString());
			else if ("raiseUnitType".equals(name))
				type.raiseUnit = UnitType.getType(reader.nextString());
			else if ("isOnlyOne".equals(name))
				type.isStatic = reader.nextBoolean();
			else if ("hasTombstone".equals(name))
				type.hasTombstone = reader.nextBoolean();
			else if ("cost".equals(name))
				type.cost = reader.nextInt();
			else if ("isFly".equals(name))
				type.isFly = reader.nextBoolean();
			else if ("canDoTwoActionAfterOne".equals(name))
				type.canDoTwoActionAfterOne = reader.nextBoolean();
			else if ("bonusOnCellWay".equals(name))
				type.bonusOnCellWay = nextCellBonuses(reader);
			else if ("bonusOnCellAttack".equals(name))
				type.bonusOnCellAttack = nextCellBonuses(reader);
			else if ("bonusOnCellDefence".equals(name))
				type.bonusOnCellDefence = nextCellBonuses(reader);
			else if ("bonusForUnitAttack".equals(name))
				type.bonusForUnitAttack = nextUnitBonuses(reader);
			else if ("bonusAfterMovingRangeType".equals(name))
				type.bonusAfterMovingRange = RangeType.getType(reader.nextString());
			else if ("bonusForUnitAfterMovingAttack".equals(name))
				type.bonusForUnitAfterMovingAttack = reader.nextInt();
			else if ("bonusForUnitAfterMovingDefence".equals(name))
				type.bonusForUnitAfterMovingDefence = reader.nextInt();
			else if ("bonusForUnitAfterAttackAttack".equals(name))
				type.bonusForUnitAfterAttackAttack = reader.nextInt();
			else if ("bonusForUnitAfterAttackDefence".equals(name))
				type.bonusForUnitAfterAttackDefence = reader.nextInt();
			else
				MyAssert.a(false);
		}
		reader.endObject();
		return type;
	}
	
	private boolean[] nextCellsArray(JsonReader reader)
	{
		boolean[] array = new boolean[CellType.number];
		String[] typeStrings = new Gson().fromJson(reader, String[].class);
		for (int i = 0; i < typeStrings.length; i++)
			array[CellType.getType(typeStrings[i]).ordinal] = true;
		return array;
	}
	
	private BonusOnCellGroup[] nextCellBonuses(JsonReader reader) throws IOException
	{
		ArrayList<BonusOnCellGroup> bonuses = new ArrayList<BonusOnCellGroup>();
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			reader.beginObject();
			String name = reader.nextName();
			int value = reader.nextInt();
			reader.endObject();
			bonuses.add(new BonusOnCellGroup(CellTypeGroup.getType(name), value));
		}
		reader.endArray();
		return bonuses.toArray(new BonusOnCellGroup[0]);
	}
	
	private BonusForUnit[] nextUnitBonuses(JsonReader reader) throws IOException
	{
		ArrayList<BonusForUnit> bonuses = new ArrayList<BonusForUnit>();
		reader.beginArray();
		while (reader.peek() == JsonToken.BEGIN_OBJECT)
		{
			reader.beginObject();
			String name = reader.nextName();
			int value = reader.nextInt();
			bonuses.add(new BonusForUnit(UnitType.getType(name), value));
			reader.endObject();
		}
		reader.endArray();
		return bonuses.toArray(new BonusForUnit[0]);
	}
	
	private void loadDefaultUnit() throws IOException
	{
		JsonReader reader = loader.getReader("defaultUnit.json");
		reader.beginObject();
		RulesLoader.loadUnitPropetries(reader, Unit.defaultUnit);
		reader.endObject();
		reader.close();
	}
	
	public static void loadUnitPropetries(JsonReader reader, Unit unit) throws IOException
	{
		while (reader.peek() == JsonToken.NAME)
		{
			String name = reader.nextName();
			if ("i".equals(name))
				unit.i = reader.nextInt();
			else if ("j".equals(name))
				unit.j = reader.nextInt();
			else if ("health".equals(name))
				unit.health = reader.nextInt();
			else if ("level".equals(name))
				unit.level = reader.nextInt();
			else if ("experience".equals(name))
				unit.experience = reader.nextInt();
			else if ("isMove".equals(name))
				unit.isMove = reader.nextBoolean();
			else if ("isTurn".equals(name))
				unit.isTurn = reader.nextBoolean();
		}
	}
	
	public static Cell nextCell(JsonReader reader, Player[] players) throws IOException
	{
		Cell cell = new Cell(Cell.defaultCell, null);
		reader.beginObject();
		while (reader.peek() == JsonToken.NAME)
		{
			String name = reader.nextName();
			if ("i".equals(name))
				cell.i = reader.nextInt();
			else if ("j".equals(name))
				cell.j = reader.nextInt();
			else if ("isDestroying".equals(name))
				cell.isDestroying = reader.nextBoolean();
			else if ("isCapture".equals(name))
			{
				cell.isCapture = reader.nextBoolean();
				if (cell.isCapture)
					cell.player = players[JsonHelper.readInt(reader, "player")];
			}
		}
		reader.endObject();
		return cell;
	}
	
}
