package ru.ancientempires.rules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map.Entry;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusCreator;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.CellTemplate;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.model.struct.Struct;
import ru.ancientempires.serializable.LoaderInfo;

public class RulesLoader {

	private FileLoader loader;
	public Rules rules = new Rules();
	public JsonDeserializationContext context;
	public LoaderInfo                 info;

	public RulesLoader(FileLoader loader) {
		this.loader = loader;
		info = new LoaderInfo(rules);
	}

	public Rules load() throws IOException {
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

	public class RulesDeserializer implements JsonDeserializer<Rules> {
		@Override
		public Rules deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			RulesLoader.this.context = context;
			JsonObject object = json.getAsJsonObject();

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
					Range[] ranges = context.deserialize(object.get("ranges"), Range[].class);
					rules.setRanges(ranges);
				
				Имхо очень прикольно =)
			*/
			rules.setRanges(context.<Range[]>deserialize(object.get("ranges"), Range[].class));

			context.deserialize(object.get("defaultUnitType"), UnitType.class);
			context.deserialize(object.get("defaultCellType"), CellType.class);

			context.deserialize(object.get("unitTypes"), UnitType[].class);
			context.deserialize(object.get("cellGroups"), CellGroup[].class);
			context.deserialize(object.get("cellTypes"), CellType[].class);

			rules.defaultGame = (JsonObject) object.get("defaultGame");
			rules.defaultPlayer = (JsonObject) object.get("defaultPlayer");
			rules.defaultPlayerComputer = (JsonObject) object.get("defaultPlayerComputer");
			return rules;
		}
	}

	public String[] getStrinsArray(JsonObject object, String name, JsonDeserializationContext context) {
		return context.deserialize(object.get(name), String[].class);
	}

	public class RangeDeserializer implements JsonDeserializer<Range> {
		@Override
		public Range deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
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

	public class UnitTypeDeserializer implements JsonDeserializer<UnitType> {
		@Override
		public UnitType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = json.getAsJsonObject();

			JsonElement baseTypeJson = object.get("baseType");
			UnitType baseType = baseTypeJson == null
					? rules.defaultUnitType
					: rules.getUnitType(baseTypeJson.getAsString());
			UnitType type = rules.getUnitType(object.get("name").getAsString());
			if (baseType != null)
				type.setProperties(baseType);

			JsonElement element;

			if ((element = object.get("specializations")) != null) {
				JsonObject specializations = element.getAsJsonObject();
				type.specializations = new HashMap<>();
				for (Entry<String, JsonElement> entry : specializations.entrySet())
					type.specializations.put(MyColor.valueOf(entry.getKey()), rules.getUnitType(entry.getValue().getAsString()));
			}
			if ((element = object.get("templateType")) != null)
				type.templateType = rules.getUnitType(element.getAsString());

			if ((element = object.get("health")) != null)
				type.healthDefault = element.getAsInt();
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
			type.repairTypes = element == null ? baseType.repairTypes : getCellTypes(element);
			element = object.get("captureTypes");
			type.captureTypes = element == null ? baseType.captureTypes : getCellTypes(element);
			element = object.get("destroyingTypes");
			type.destroyingTypes = element == null ? baseType.destroyingTypes : getCellTypes(element);

			element = object.get("attackRange");
			type.attackRange = element == null ? baseType.attackRange : getRange(element);
			element = object.get("attackRangeReverse");
			type.attackRangeReverse = element == null ? baseType.attackRangeReverse : getRange(element);

			element = object.get("raiseRange");
			type.raiseRange = element == null ? baseType.raiseRange : getRange(element);
			element = object.get("raiseType");
			type.raiseType = element == null ? baseType.raiseType : rules.getUnitType(element.getAsString());

			element = object.get("isStatic");
			type.isStatic = element == null ? baseType.isStatic : element.getAsBoolean();
			element = object.get("hasTombstone");
			type.hasTombstone = element == null ? baseType.hasTombstone : element.getAsBoolean();
			element = object.get("canDoTwoActionAfterOne");
			type.canDoTwoActionAfterOne = element == null ? baseType.canDoTwoActionAfterOne : element.getAsBoolean();
			element = object.get("isFly");
			type.isFly = element == null ? baseType.isFly : element.getAsBoolean();

			element = object.get("bonuses");
			type.bonuses = element == null ? baseType.bonuses : context.<Bonus[]>deserialize(element, Bonus[].class);
			element = object.get("creators");
			type.creators = element == null ? baseType.creators : context.<BonusCreator[]>deserialize(element, BonusCreator[].class);

			return type;
		}
	}

	public CellType[] getCellTypes(JsonElement element) {
		String[] names = context.deserialize(element, String[].class);
		CellType[] types = new CellType[names.length];
		for (int i = 0; i < types.length; i++)
			types[i] = rules.getCellType(names[i]);
		return types;
	}

	public Range getRange(JsonElement element) {
		return rules.getRange(element.getAsString());
	}

	public class CellGroupDeserializer implements JsonDeserializer<CellGroup> {
		@Override
		public CellGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = json.getAsJsonObject();
			CellGroup group = rules.getCellGroup(object.get("name").getAsString());
			group.baseType = context.deserialize(object.get("baseType"), CellType.class);
			group.setTypes(getCellTypes(object.get("types")));
			return group;
		}
	}

	public class CellTypeDeserializer implements JsonDeserializer<CellType> {
		@Override
		public CellType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			JsonObject object = json.getAsJsonObject();

			JsonElement baseTypeJson = object.get("baseType");
			CellType baseType = baseTypeJson == null
					? rules.defaultCellType
					: rules.getCellType(baseTypeJson.getAsString());
			CellType type = rules.getCellType(object.get("name").getAsString());
			if (baseType != null)
				type.setProperties(baseType);

			JsonElement element;

			type.isDefault = object.get("isDefault").getAsBoolean();
			if ((element = object.get("steps")) != null)
				type.steps = element.getAsInt();
			if ((element = object.get("earn")) != null)
				type.earn = element.getAsInt();
			if ((element = object.get("defense")) != null)
				type.defense = element.getAsInt();
			if ((element = object.get("buyTypes")) != null)
				type.buyTypes = getUnitTypes(element);
			if ((element = object.get("isHealing")) != null)
				type.isHealing = element.getAsBoolean();
			if ((element = object.get("isCapturing")) != null)
				type.isCapturing = element.getAsBoolean();
			if ((element = object.get("destroyingType")) != null)
				type.destroyingType = rules.getCellType(element.getAsString());
			if ((element = object.get("repairType")) != null)
				type.repairType = rules.getCellType(element.getAsString());
			if ((element = object.get("template")) != null)
				type.template = CellTemplate.fromJSON(element, RulesLoader.this, type);
			type.mapEditorFrequency = object.get("mapEditorFrequency").getAsInt();

			try {
				//if ((element = object.get("struct")) != null)
				//	type.struct = Struct.fromJSON(element, info);
				if (object.has("struct"))
					type.struct = info.fromJson((JsonObject) object.get("struct"), Struct.class);
			} catch (Exception e) {
				MyAssert.a(false);
				e.printStackTrace();
			}
			return type;
		}
	}

	public UnitType[] getUnitTypes(JsonElement element) {
		String[] names = context.deserialize(element, String[].class);
		UnitType[] types = new UnitType[names.length];
		for (int i = 0; i < types.length; i++)
			types[i] = rules.getUnitType(names[i]);
		return types;
	}

	public class BonusDeserializer implements JsonDeserializer<Bonus> {
		@Override
		public Bonus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			try {
				return info.fromJson(((JsonObject) json), Bonus.class);
			} catch (Exception e) {
				e.printStackTrace();
				MyAssert.a(false);
				return null;
			}
		}
	}

	public class BonusCreatorDeserializer implements JsonDeserializer<BonusCreator> {
		@Override
		public BonusCreator deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			try {
				JsonObject object = json.getAsJsonObject();
				int ordinal = object.get("type").getAsInt();
				BonusCreator creator = BonusCreator.classes.get(ordinal).newInstance();
				creator.loadJSON(object, rules, context);
				return creator;
			} catch (Exception e) {
				e.printStackTrace();
				MyAssert.a(false);
				return null;
			}
		}
	}

}
