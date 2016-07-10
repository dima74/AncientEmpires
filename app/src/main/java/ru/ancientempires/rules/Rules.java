package ru.ancientempires.rules;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Unit;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class Rules
{
	
	public String name;
	public String version;
	public String author;

	public Unit     defaultUnit;
	public UnitType defaultUnitType;
	public CellType defaultCellType;

	public Range[]                ranges;
	public HashMap<String, Range> rangesMap;
	public Range                  rangeMax;

	public UnitType[] unitTypes;
	public HashMap<String, UnitType> unitTypesMap = new HashMap<>();

	public CellGroup[] cellGroups;
	public HashMap<String, CellGroup> cellGroupsMap = new HashMap<>();
	public CellType[] cellTypes;
	public HashMap<String, CellType> cellTypesMap = new HashMap<>();

	public JsonObject defaultGame;
	public JsonObject defaultPlayer;
	public JsonObject defaultPlayerComputer;

	public void preInitUnitTypes(String[] names)
	{
		unitTypes = new UnitType[names.length];
		for (int i = 0; i < names.length; i++)
			unitTypes[i] = new UnitType(names[i], i);
		for (UnitType type : unitTypes)
			unitTypesMap.put(type.name, type);
		defaultUnitType = getUnitType("DEFAULT");
	}
	
	public void preInitCellTypes(String[] names)
	{
		cellTypes = new CellType[names.length];
		for (int i = 0; i < names.length; i++)
			cellTypes[i] = new CellType(names[i], i);
		for (CellType type : cellTypes)
			cellTypesMap.put(type.name, type);
		defaultCellType = getCellType("DEFAULT");
	}
	
	public void preInitCellGroups(String[] names)
	{
		cellGroups = new CellGroup[names.length];
		for (int i = 0; i < names.length; i++)
			cellGroups[i] = new CellGroup(this, names[i], i);
		for (CellGroup group : cellGroups)
			cellGroupsMap.put(group.name, group);
	}
	
	public UnitType getUnitType(String name)
	{
		MyAssert.a(unitTypesMap.containsKey(name));
		return unitTypesMap.get(name);
	}
	
	public CellType getCellType(String name)
	{
		MyAssert.a(cellTypesMap.containsKey(name));
		return cellTypesMap.get(name);
	}
	
	public CellGroup getCellGroup(String name)
	{
		MyAssert.a(cellGroupsMap.containsKey(name));
		return cellGroupsMap.get(name);
	}
	
	public void setRanges(Range[] ranges)
	{
		this.ranges = ranges;
		
		rangesMap = new HashMap<>();
		for (Range range : ranges)
			rangesMap.put(range.name, range);

		int maxRadius = 0;
		for (Range type : ranges)
			maxRadius = Math.max(maxRadius, type.radius);
		int maxSize = maxRadius * 2 + 1;
		boolean[][] table = new boolean[maxSize][maxSize];
		for (Range range : ranges)
			for (int i = -range.radius; i <= range.radius; i++)
				for (int j = -range.radius; j <= range.radius; j++)
					table[maxRadius + i][maxRadius + j] |= range.table[range.radius + i][range.radius + j];
		rangeMax = new Range("MAX_RANGE", table);
	}
	
	public Range getRange(String name)
	{
		return rangesMap.get(name);
	}
	
	public String[] getAllUnitTypes()
	{
		String[] names = new String[unitTypes.length];
		for (int i = 0; i < names.length; i++)
			names[i] = unitTypes[i].name;
		return names;
	}
	
	public String[] getAllCellTypes()
	{
		String[] names = new String[cellTypes.length];
		for (int i = 0; i < names.length; i++)
			names[i] = cellTypes[i].name;
		return names;
	}
	
	public String[] getAllCellGroups()
	{
		String[] names = new String[cellGroups.length];
		for (int i = 0; i < names.length; i++)
			names[i] = cellGroups[i].name;
		return names;
	}
	
	public int numberUnitTypes()
	{
		return unitTypes.length;
	}
	
	public int numberCellTypes()
	{
		return cellTypes.length;
	}
	
	public JsonArray getDefaultsPlayers(int numberPlayers)
	{
		JsonArray array = new JsonArray();
		for (int i = 0; i < numberPlayers; i++)
			array.add(SerializableJsonHelper.deepCopy(numberPlayers == 2 && i == 1 ? defaultPlayerComputer : defaultPlayer));
		for (int i = 0; i < array.size(); i++)
		{
			JsonObject object = (JsonObject) array.get(i);
			object.addProperty("team", i);
			object.addProperty("color", MyColor.playersColors()[i].name());
		}
		return array;
	}

}
