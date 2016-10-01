package ru.ancientempires.rules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map.Entry;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusCreator;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.UnitType;

public class RulesSaver
{

	public FileLoader loader;
	public Rules      rules;

	public RulesSaver(FileLoader loader, Rules rules)
	{
		this.loader = loader;
		this.rules = rules;
	}

	public void save(String name) throws IOException
	{
		new File("assets/rules/").mkdir();
		JsonWriter writer = loader.getWriter(name);
		writer.setIndent("\t");
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Rules.class, new RulesSerializer())
				.registerTypeAdapter(Range.class, new RangeSerializer())
				.registerTypeAdapter(UnitType.class, new UnitTypeSerializer())
				.registerTypeAdapter(CellGroup.class, new CellGroupSerializer())
				.registerTypeAdapter(CellType.class, new CellTypeSerializer())
				.registerTypeHierarchyAdapter(Bonus.class, new BonusSerializer())
				.registerTypeHierarchyAdapter(BonusCreator.class, new BonusCreatorSerializer())
				// .setPrettyPrinting() // не влияет
				.create();
		gson.toJson(rules, Rules.class, writer);
		writer.close();
		System.exit(0);
	}

	public class RulesSerializer implements JsonSerializer<Rules>
	{
		@Override
		public JsonElement serialize(Rules rules, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject result = new JsonObject();
			result.addProperty("name", rules.name);
			result.addProperty("version", rules.version);
			result.addProperty("author", rules.author);

			result.add("allUnitTypes", context.serialize(rules.getAllUnitTypes()));
			result.add("allCellTypes", context.serialize(rules.getAllCellTypes()));
			result.add("allCellGroups", context.serialize(rules.getAllCellGroups()));
			result.add("ranges", context.serialize(rules.ranges));

			result.add("defaultUnitType", context.serialize(rules.defaultUnitType));
			result.add("defaultCellType", context.serialize(rules.defaultCellType));

			result.add("unitTypes", context.serialize(rules.unitTypes));
			result.add("cellGroups", context.serialize(rules.cellGroups));
			result.add("cellTypes", context.serialize(rules.cellTypes));

			result.add("defaultGame", rules.defaultGame);
			result.add("defaultPlayer", rules.defaultPlayer);
			result.add("defaultPlayerComputer", rules.defaultPlayerComputer);
			return result;
		}
	}

	public class RangeSerializer implements JsonSerializer<Range>
	{
		@Override
		public JsonElement serialize(Range range, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject result = new JsonObject();
			result.addProperty("name", range.name);

			JsonArray array = new JsonArray();
			boolean[][] table = range.table;
			for (boolean[] line : table)
			{
				String s = "";
				for (boolean b : line)
					s += b ? '1' : '0';
				array.add(s);
			}
			result.add("table", array);
			return result;
		}
	}

	public class UnitTypeSerializer implements JsonSerializer<UnitType>
	{
		@Override
		public JsonElement serialize(UnitType type, Type typeOfSrc, JsonSerializationContext context)
		{
			UnitType defaultType = type.baseType;

			JsonObject result = new JsonObject();
			if (type == defaultType || type.baseType != defaultType.baseType)
				result.addProperty("baseType", type.baseType.name);
			result.addProperty("name", type.name);

			if (type.specializations != null)
			{
				JsonObject specializations = new JsonObject();
				for (Entry<MyColor, UnitType> entry : type.specializations.entrySet())
					specializations.addProperty(entry.getKey().name(), entry.getValue().name);
				result.add("specializations", specializations);
			}
			if (type.templateType != null)
				result.addProperty("templateType", type.templateType.name);

			if (type == defaultType || type.healthDefault != defaultType.healthDefault)
				result.addProperty("health", type.healthDefault);
			if (type == defaultType || type.attackMin != defaultType.attackMin)
				result.addProperty("attackMin", type.attackMin);
			if (type == defaultType || type.attackMax != defaultType.attackMax)
				result.addProperty("attackMax", type.attackMax);
			if (type == defaultType || type.defence != defaultType.defence)
				result.addProperty("defence", type.defence);
			if (type == defaultType || type.moveRadius != defaultType.moveRadius)
				result.addProperty("moveRadius", type.moveRadius);
			if (type == defaultType || type.cost != defaultType.cost)
				result.addProperty("cost", type.cost);
			if (type == defaultType || !type.repairTypes.equals(defaultType.repairTypes))
				result.add("repairTypes", toArray(type.repairTypes));
			if (type == defaultType || !type.captureTypes.equals(defaultType.captureTypes))
				result.add("captureTypes", toArray(type.captureTypes));
			if (type == defaultType || !type.destroyingTypes.equals(defaultType.destroyingTypes))
				result.add("destroyingTypes", toArray(type.destroyingTypes));
			if (type == defaultType || type.attackRange != defaultType.attackRange)
				result.addProperty("attackRange", type.attackRange.name);
			if (type == defaultType || type.attackRangeReverse != defaultType.attackRangeReverse)
				result.addProperty("attackRangeReverse", type.attackRangeReverse.name);
			if (type == defaultType || type.raiseRange != defaultType.raiseRange)
				result.addProperty("raiseRange", type.raiseRange.name);
			if (type.raiseType != null && (type == defaultType || type.raiseType != defaultType.raiseType))
				result.addProperty("raiseType", type.raiseType.name);
			if (type == defaultType || type.isStatic != defaultType.isStatic)
				result.addProperty("isStatic", type.isStatic);
			if (type == defaultType || type.hasTombstone != defaultType.hasTombstone)
				result.addProperty("hasTombstone", type.hasTombstone);
			if (type == defaultType || type.canDoTwoActionAfterOne != defaultType.canDoTwoActionAfterOne)
				result.addProperty("canDoTwoActionAfterOne", type.canDoTwoActionAfterOne);
			if (type == defaultType || type.isFly != defaultType.isFly)
				result.addProperty("isFly", type.isFly);
			if (type == defaultType || type.bonuses != defaultType.bonuses)
				result.add("bonuses", context.serialize(type.bonuses));
			if (type == defaultType || type.creators != defaultType.creators)
				result.add("creators", context.serialize(type.creators));
			return result;
		}
	}

	public JsonArray toArray(CellType[] types)
	{
		JsonArray array = new JsonArray();
		for (CellType type : types)
			array.add(type.name);
		return array;
	}

	public class CellGroupSerializer implements JsonSerializer<CellGroup>
	{
		@Override
		public JsonElement serialize(CellGroup group, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject result = new JsonObject();
			result.addProperty("name", group.name);
			result.add("baseType", context.serialize(group.baseType));
			result.add("types", toArray(group.types));
			return result;
		}
	}

	public class CellTypeSerializer implements JsonSerializer<CellType>
	{
		@Override
		public JsonElement serialize(CellType type, Type typeOfSrc, JsonSerializationContext context)
		{
			CellType defaultType = type.baseType;

			JsonObject result = new JsonObject();
			if (type == defaultType || type.baseType != defaultType.baseType)
				result.addProperty("baseType", type.baseType.name);
			result.addProperty("isDefault", type.isDefault);
			result.addProperty("name", type.name);
			if (type == defaultType || type.steps != defaultType.steps)
				result.addProperty("steps", type.steps);
			if (type == defaultType || type.earn != defaultType.earn)
				result.addProperty("earn", type.earn);
			if (type == defaultType || type.defense != defaultType.defense)
				result.addProperty("defense", type.defense);
			if (type == defaultType || type.buyTypes != defaultType.buyTypes)
				result.add("buyTypes", toArray(type.buyTypes));
			if (type == defaultType || type.isHealing != defaultType.isHealing)
				result.addProperty("isHealing", type.isHealing);
			if (type == defaultType || type.isCapturing != defaultType.isCapturing)
				result.addProperty("isCapturing", type.isCapturing);
			if (type.destroyingType != null && (type == defaultType || type.destroyingType != defaultType.destroyingType))
				result.addProperty("destroyingType", type.destroyingType.name);
			if (type.repairType != null && (type == defaultType || type.repairType != defaultType.repairType))
				result.addProperty("repairType", type.repairType.name);
			if (type.template != null)
				result.add("template", type.template.toJSON(RulesSaver.this));
			result.addProperty("mapEditorFrequency", type.mapEditorFrequency);

			//if (type.struct != null)
			//	result.add("struct", type.struct.toJSON());
			try
			{
				if (type.struct != null)
					result.add("struct", type.struct.toJson());
			}
			catch (Exception e)
			{
				MyAssert.a(false);
				e.printStackTrace();
			}
			return result;
		}
	}

	public JsonArray toArray(UnitType[] types)
	{
		JsonArray array = new JsonArray();
		for (UnitType type : types)
			array.add(type.name);
		return array;
	}

	public class BonusSerializer implements JsonSerializer<Bonus>
	{
		@Override
		public JsonElement serialize(Bonus bonus, Type typeOfSrc, JsonSerializationContext context)
		{
			try
			{
				return bonus.toJson();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				MyAssert.a(false);
			}
			return null;
		}
	}

	public class BonusCreatorSerializer implements JsonSerializer<BonusCreator>
	{
		@Override
		public JsonElement serialize(BonusCreator creator, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject result = new JsonObject();
			result.addProperty("type", creator.ordinal());
			creator.saveJSON(result, context);
			return result;
		}
	}

}
