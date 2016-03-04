package ru.ancientempires.rules;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusCreator;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.helpers.FileLoader;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.UnitType;

public class RulesLoader
{
	
	private FileLoader	loader;
	private Rules		rules;
						
	public RulesLoader(FileLoader loader)
	{
		this.loader = loader;
	}
	
	public Rules load() throws IOException
	{
		JsonReader reader = loader.getReader("rules.json");
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Rules.class, new RulesDeserializer())
				.registerTypeAdapter(Range.class, new RangeDeserializer())
				.registerTypeAdapter(UnitType.class, new UnitTypeDeserializer())
				.registerTypeAdapter(CellGroup.class, new CellGroupDeserializer())
				.registerTypeAdapter(CellType.class, new CellTypeDeserializer())
				.registerTypeHierarchyAdapter(Bonus.class, new BonusDeserializer())
				.registerTypeHierarchyAdapter(BonusCreator.class, new BonusCreatorDeserializer())
				.create();
		gson.fromJson(reader, Rules.class);
		reader.close();
		return rules;
	}
	
	public class RulesDeserializer implements JsonDeserializer<Rules>
	{
		@Override
		public Rules deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();
			
			rules = new Rules();
			rules.name = object.get("name").getAsString();
			rules.version = object.get("version").getAsString();
			rules.author = object.get("author").getAsString();
			
			rules.preInitUnitTypes(getStrinsArray(object, "allUnitTypes", context));
			rules.preInitCellTypes(getStrinsArray(object, "allCellTypes", context));
			rules.preInitCellGroups(getStrinsArray(object, "allCellGroups", context));
			
			/*
				здесь очень забавная ситуация, компилятор Java SE может вычислить,
					что используется deserialize c типом Range[], а вот андроидовский не может.
				SE'шный Вычисляет мне кажется по тому,
					что аргумент функции setRanges имеет тип Range[]
				
				То есть вот так работает только на Java SE:
					rules.setRanges(context.deserialize(object.get("ranges"), Range[].class));
				
				А вот так везде:
					Rules rules = context.deserialize(object.get("ranges"), Range[].class);
					rules.setRanges(rules);
				
				Имхо очень прикольно =)
			*/
			rules.setRanges(context.<Range[]> deserialize(object.get("ranges"), Range[].class));
			
			context.deserialize(object.get("defaultUnitType"), UnitType.class);
			context.deserialize(object.get("defaultCellType"), CellType.class);
			
			context.deserialize(object.get("unitTypes"), UnitType[].class);
			context.deserialize(object.get("cellGroups"), CellGroup[].class);
			context.deserialize(object.get("cellTypes"), CellType[].class);
			
			return rules;
		}
	}
	
	public String[] getStrinsArray(JsonObject object, String name, JsonDeserializationContext context)
	{
		return context.deserialize(object.get(name), String[].class);
	}
	
	public class RangeDeserializer implements JsonDeserializer<Range>
	{
		@Override
		public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();
			String name = object.get("name").getAsString();
			String[] strings = context.deserialize(object.get("table"), String[].class);
			int size = strings.length;
			boolean[][] table = new boolean[size][size];
			for (int i = 0; i < size; i++)
				for (int j = 0; j < size; j++)
					table[i][j] = strings[i].charAt(j) == '1';
			return new Range(name, table);
		}
	}
	
	public class UnitTypeDeserializer implements JsonDeserializer<UnitType>
	{
		@Override
		public UnitType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();
			
			JsonElement baseTypeJson = object.get("baseType");
			UnitType baseType = baseTypeJson == null
					? rules.defaultUnitType
					: rules.getUnitType(baseTypeJson.getAsString());
			UnitType type = rules.getUnitType(object.get("name").getAsString());
			if (baseType != null)
				type.setProperties(baseType);
				
			JsonElement element;
			
			element = object.get("health");
			type.healthDefault = element == null ? baseType.healthDefault : element.getAsInt();
			element = object.get("attackMin");
			type.attackMin = element == null ? baseType.attackMin : element.getAsInt();
			element = object.get("attackMax");
			type.attackMax = element == null ? baseType.attackMax : element.getAsInt();
			element = object.get("defence");
			type.defence = element == null ? baseType.defence : element.getAsInt();
			element = object.get("moveRadius");
			type.moveRadius = element == null ? baseType.moveRadius : element.getAsInt();
			element = object.get("cost");
			type.cost = element == null ? baseType.cost : element.getAsInt();
			
			element = object.get("repairTypes");
			type.repairTypes = element == null ? baseType.repairTypes : getCellTypes(element, context);
			element = object.get("captureTypes");
			type.captureTypes = element == null ? baseType.captureTypes : getCellTypes(element, context);
			element = object.get("destroyingTypes");
			type.destroyingTypes = element == null ? baseType.destroyingTypes : getCellTypes(element, context);
			
			element = object.get("attackRange");
			type.attackRange = element == null ? baseType.attackRange : getRange(element);
			element = object.get("attackRangeReverse");
			type.attackRangeReverse = element == null ? baseType.attackRangeReverse : getRange(element);
			
			element = object.get("raiseRange");
			type.raiseRange = element == null ? baseType.raiseRange : getRange(element);
			element = object.get("raiseUnit");
			type.raiseUnit = element == null ? baseType.raiseUnit : rules.getUnitType(element.getAsString());
			
			element = object.get("isStatic");
			type.isStatic = element == null ? baseType.isStatic : element.getAsBoolean();
			element = object.get("hasTombstone");
			type.hasTombstone = element == null ? baseType.hasTombstone : element.getAsBoolean();
			element = object.get("canDoTwoActionAfterOne");
			type.canDoTwoActionAfterOne = element == null ? baseType.canDoTwoActionAfterOne : element.getAsBoolean();
			element = object.get("isFly");
			type.isFly = element == null ? baseType.isFly : element.getAsBoolean();
			
			element = object.get("bonuses");
			type.bonuses = element == null ? baseType.bonuses : context.<Bonus[]> deserialize(element, Bonus[].class);
			element = object.get("creators");
			type.creators = element == null ? baseType.creators : context.<BonusCreator[]> deserialize(element, BonusCreator[].class);
			
			return type;
		}
	}
	
	public CellType[] getCellTypes(JsonElement element, JsonDeserializationContext context)
	{
		String[] names = context.deserialize(element, String[].class);
		CellType[] types = new CellType[names.length];
		for (int i = 0; i < types.length; i++)
			types[i] = rules.getCellType(names[i]);
		return types;
	}
	
	public Range getRange(JsonElement element)
	{
		return rules.getRange(element.getAsString());
	}
	
	public class CellGroupDeserializer implements JsonDeserializer<CellGroup>
	{
		@Override
		public CellGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();
			CellGroup group = rules.getCellGroup(object.get("name").getAsString());
			group.baseType = context.deserialize(object.get("baseType"), CellType.class);
			group.setTypes(getCellTypes(object.get("types"), context));
			return group;
		}
	}
	
	public class CellTypeDeserializer implements JsonDeserializer<CellType>
	{
		@Override
		public CellType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();
			
			JsonElement baseTypeJson = object.get("baseType");
			CellType baseType = baseTypeJson == null
					? rules.defaultCellType
					: rules.getCellType(baseTypeJson.getAsString());
			CellType type = rules.getCellType(object.get("name").getAsString());
			if (baseType != null)
				type.setProperties(baseType);
				
			JsonElement element;
			
			element = object.get("steps");
			type.steps = element == null ? baseType.steps : element.getAsInt();
			element = object.get("earn");
			type.earn = element == null ? baseType.earn : element.getAsInt();
			element = object.get("defense");
			type.defense = element == null ? baseType.defense : element.getAsInt();
			element = object.get("buyTypes");
			type.buyTypes = element == null ? baseType.buyTypes : getUnitTypes(element, context);
			element = object.get("isCapturing");
			type.isCapturing = element == null ? baseType.isCapturing : element.getAsBoolean();
			element = object.get("isDestroying");
			type.isDestroying = element == null ? baseType.isDestroying : element.getAsBoolean();
			element = object.get("isHeal");
			type.isHealing = element == null ? baseType.isHealing : element.getAsBoolean();
			return type;
		}
	}
	
	public UnitType[] getUnitTypes(JsonElement element, JsonDeserializationContext context)
	{
		String[] names = context.deserialize(element, String[].class);
		UnitType[] types = new UnitType[names.length];
		for (int i = 0; i < types.length; i++)
			types[i] = rules.getUnitType(names[i]);
		return types;
	}
	
	public class BonusDeserializer implements JsonDeserializer<Bonus>
	{
		@Override
		public Bonus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			try
			{
				return Bonus.loadJsonBase(json.getAsJsonObject(), rules);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				MyAssert.a(false);
				return null;
			}
		}
	}
	
	public class BonusCreatorDeserializer implements JsonDeserializer<BonusCreator>
	{
		@Override
		public BonusCreator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			try
			{
				JsonObject object = json.getAsJsonObject();
				int ordinal = object.get("type").getAsInt();
				BonusCreator creator = BonusCreator.classes.get(ordinal).newInstance();
				creator.loadJSON(object, rules, context);
				return creator;
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
				MyAssert.a(false);
				return null;
			}
		}
	}
	
}
