package ru.ancientempires.rules;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;

import ru.ancientempires.MyColor;
import ru.ancientempires.bonuses.Bonus;
import ru.ancientempires.bonuses.BonusAttackAlways;
import ru.ancientempires.bonuses.BonusAttackForUnit;
import ru.ancientempires.bonuses.BonusCost;
import ru.ancientempires.bonuses.BonusCreator;
import ru.ancientempires.bonuses.BonusCreatorDireWolf;
import ru.ancientempires.bonuses.BonusCreatorWisp;
import ru.ancientempires.bonuses.BonusLevel;
import ru.ancientempires.bonuses.BonusMoveToCellGroup;
import ru.ancientempires.bonuses.BonusMoveToCellType;
import ru.ancientempires.bonuses.BonusOnCellGroup;
import ru.ancientempires.model.CellGroup;
import ru.ancientempires.model.CellTemplate;
import ru.ancientempires.model.CellTemplateType;
import ru.ancientempires.model.CellType;
import ru.ancientempires.model.Player;
import ru.ancientempires.model.PlayerType;
import ru.ancientempires.model.Range;
import ru.ancientempires.model.Team;
import ru.ancientempires.model.UnitType;
import ru.ancientempires.model.struct.StructCitadel;
import ru.ancientempires.serializable.SerializableJsonHelper;

public class DefaultRules {

	public Rules rules = new Rules();

	public Rules create() throws IOException {
		/*
		Files
				.lines(new File("1.txt").toPath())
				.map(s -> s.trim().replaceAll("^.*\\s(.*);$", "$1"))
				.filter(s -> !s.isEmpty())
				.map(s -> "element = object.get(\"" + s + "\");\n" +
						"type." + s + " = element == null ? baseType." + s + " : element.getAsInt();")
				.forEach(System.out::println);
		System.exit(0);
		// */
		// printAll();
		// printGroup("way", "way");
		// printGroup("water", "water");

		rules.name = "DEFAULT";
		rules.version = "1.0";
		rules.author = "Macrospace";

		rules.preInitUnitTypes(unitTypes);
		rules.preInitCellTypes(cellTypes);
		rules.preInitCellGroups(cellGroups);
		createRanges();

		initVariables();

		createUnitTypes();
		createCellGroups();
		createCellTypes();
		createDefaultGame();
		createDefaultPlayers();
		createMapEditorFrequencies();

		return rules;
	}

	public String[] ranges = new String[]
			{
					"EMPTY",
					"DEFAULT",
					"ARCHER",
					"CATAPULT"
			};

	public String[] unitTypes  = new String[]
			{
					"DEFAULT",
					"KING",
					"SOLDIER",
					"ARCHER",
					"ELEMENTAL",
					"SORCERESS",
					"WISP",
					"DIRE_WOLF",
					"GOLEM",
					"CATAPULT",
					"DRAGON",
					"SKELETON",
					"CRYSTAL",
					"KING_GALAMAR",
					"KING_VALADORN",
					"KING_DEMONLORD",
					"KING_SAETH"
			};
	public String[] cellTypes  = new String[]
			{
					"DEFAULT",
					"MOUNT",
					"HILL",
					"TWO_TREES",
					"THREE_TREES",
					"PLAIN",
					"CITADEL_LEFT",
					"CITADEL_RIGHT",
					"CITADEL_UP",
					"BUILDING",
					"BUILDING_DESTROYING",
					"CASTLE",
					"CAMP",
					"TEMPLE",
					"CITADEL",
					"WAY",
					"WATER_WAY_HORIZONTAL",
					"WATER_WAY_VERTICAL",
					"WATER",
					"WATER_SPARKS",

					"DEFAULT_GROUP",
					"HEALING_GROUP",
					"BUILDINGS_GROUP",
					"WATER_WAY_GROUP",
					"WATER_GROUP"
			};
	public String[] cellGroups = new String[]
			{
					"DEFAULT",
					"HEALING",
					"BUILDINGS",
					"WATER_WAY",
					"WATER",
			};

	/*
	private void printGroup(String group, String prefix)
	{
		String strings = Arrays
				.stream(cellTypes)
				.map(String::toLowerCase)
				.filter(s -> s.contains(prefix))
				.collect(Collectors.joining(",\n"));

		System.out.println();
		System.out.println(group + ".setTypes(\n" + strings + ");");
	}
	
	public void printAll()
	{
		Arrays.stream(ranges)
				.map(s -> "public Range " + s.toLowerCase() + "Range;")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(unitTypes)
				.map(s -> "public UnitType " + s.toLowerCase().replace("default", "defaultUnitType") + ";")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(cellGroups)
				.map(s -> "public CellGroup " + s.toLowerCase().replace("default", "defaultGroup") + ";")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(cellTypes)
				.map(s -> "public CellType " + s.toLowerCase().replaceAll("^default$", "defaultCellType") + ";")
				.forEach(System.out::println);
		System.out.println();
		
		System.out.println("public void initVariables()");
		System.out.println("{");
		Arrays.stream(ranges)
				.map(s -> s.toLowerCase() + "Range = rules.getRange(\"" + s + "\");")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(unitTypes)
				.map(s -> s.toLowerCase().replace("default", "defaultUnitType") + " = rules.getUnitType(\"" + s + "\");")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(cellGroups)
				.map(s -> s.toLowerCase().replace("default", "defaultGroup") + " = rules.getCellGroup(\"" + s + "\");")
				.forEach(System.out::println);
		System.out.println();
		Arrays.stream(cellTypes)
				.map(s -> s.toLowerCase().replaceAll("^default$", "defaultCellType") + " = rules.getCellType(\"" + s + "\");")
				.forEach(System.out::println);
		System.out.println("}");
		System.exit(0);
	}
	//*/

	// BEGIN AUTO GENERATE
	public Range emptyRange;
	public Range defaultRange;
	public Range archerRange;
	public Range catapultRange;

	public UnitType defaultUnitType;
	public UnitType king;
	public UnitType soldier;
	public UnitType archer;
	public UnitType elemental;
	public UnitType sorceress;
	public UnitType wisp;
	public UnitType dire_wolf;
	public UnitType golem;
	public UnitType catapult;
	public UnitType dragon;
	public UnitType skeleton;
	public UnitType crystal;
	public UnitType king_galamar;
	public UnitType king_valadorn;
	public UnitType king_demonlord;
	public UnitType king_saeth;

	public CellGroup defaultGroup;
	public CellGroup healing;
	public CellGroup buildings;
	public CellGroup water_way;
	public CellGroup waterGroup;

	public CellType defaultCellType;
	public CellType mount;
	public CellType hill;
	public CellType two_trees;
	public CellType three_trees;
	public CellType plain;
	public CellType citadel_left;
	public CellType citadel_right;
	public CellType citadel_up;
	public CellType building;
	public CellType building_destroying;
	public CellType castle;
	public CellType camp;
	public CellType temple;
	public CellType citadel;
	public CellType way;
	public CellType water_way_horizontal;
	public CellType water_way_vertical;
	public CellType water;
	public CellType water_sparks;
	public CellType default_group;
	public CellType healing_group;
	public CellType buildings_group;
	public CellType water_way_group;
	public CellType water_group;

	public void initVariables() {
		emptyRange = rules.getRange("EMPTY");
		defaultRange = rules.getRange("DEFAULT");
		archerRange = rules.getRange("ARCHER");
		catapultRange = rules.getRange("CATAPULT");

		defaultUnitType = rules.getUnitType("DEFAULT");
		king = rules.getUnitType("KING");
		soldier = rules.getUnitType("SOLDIER");
		archer = rules.getUnitType("ARCHER");
		elemental = rules.getUnitType("ELEMENTAL");
		sorceress = rules.getUnitType("SORCERESS");
		wisp = rules.getUnitType("WISP");
		dire_wolf = rules.getUnitType("DIRE_WOLF");
		golem = rules.getUnitType("GOLEM");
		catapult = rules.getUnitType("CATAPULT");
		dragon = rules.getUnitType("DRAGON");
		skeleton = rules.getUnitType("SKELETON");
		crystal = rules.getUnitType("CRYSTAL");
		king_galamar = rules.getUnitType("KING_GALAMAR");
		king_valadorn = rules.getUnitType("KING_VALADORN");
		king_demonlord = rules.getUnitType("KING_DEMONLORD");
		king_saeth = rules.getUnitType("KING_SAETH");

		defaultGroup = rules.getCellGroup("DEFAULT");
		healing = rules.getCellGroup("HEALING");
		buildings = rules.getCellGroup("BUILDINGS");
		water_way = rules.getCellGroup("WATER_WAY");
		waterGroup = rules.getCellGroup("WATER");

		defaultCellType = rules.getCellType("DEFAULT");
		mount = rules.getCellType("MOUNT");
		hill = rules.getCellType("HILL");
		two_trees = rules.getCellType("TWO_TREES");
		three_trees = rules.getCellType("THREE_TREES");
		plain = rules.getCellType("PLAIN");
		citadel_left = rules.getCellType("CITADEL_LEFT");
		citadel_right = rules.getCellType("CITADEL_RIGHT");
		citadel_up = rules.getCellType("CITADEL_UP");
		building = rules.getCellType("BUILDING");
		building_destroying = rules.getCellType("BUILDING_DESTROYING");
		castle = rules.getCellType("CASTLE");
		camp = rules.getCellType("CAMP");
		temple = rules.getCellType("TEMPLE");
		citadel = rules.getCellType("CITADEL");
		way = rules.getCellType("WAY");
		water_way_horizontal = rules.getCellType("WATER_WAY_HORIZONTAL");
		water_way_vertical = rules.getCellType("WATER_WAY_VERTICAL");
		water = rules.getCellType("WATER");
		water_sparks = rules.getCellType("WATER_SPARKS");
		default_group = rules.getCellType("DEFAULT_GROUP");
		healing_group = rules.getCellType("HEALING_GROUP");
		buildings_group = rules.getCellType("BUILDINGS_GROUP");
		water_way_group = rules.getCellType("WATER_WAY_GROUP");
		water_group = rules.getCellType("WATER_GROUP");
	}

	// END AUTO GENERATE

	public void createRanges() {
		Range[] ranges = new Range[]
				{
						new Range("EMPTY", 1, 0),
						new Range("DEFAULT", 1, 1),
						new Range("ARCHER", 1, 2),
						new Range("CATAPULT", 2, 4)
				};
		rules.setRanges(ranges);
	}

	public void createUnitTypes() {
		createDefaultUnitType();
		for (UnitType type : rules.unitTypes)
			type.setProperties(rules.defaultUnitType);

		soldier.cost = 150;
		soldier.captureTypes = getCellTypes("BUILDING");
		soldier.repairTypes = getCellTypes("BUILDING_DESTROYING");

		archer.cost = 250;
		archer.attackRange = archerRange;
		archer.addBonuses(new BonusAttackForUnit(dragon, 15, 0));

		elemental.defence = 10;
		elemental.cost = 300;
		elemental.addBonuses(
				new BonusOnCellGroup(waterGroup, 10, 15),
				new BonusMoveToCellGroup(waterGroup, -2));

		sorceress.attackMin = 40;
		sorceress.attackMax = 45;
		sorceress.cost = 400;
		sorceress.raiseRange = defaultRange;
		sorceress.raiseType = skeleton;

		wisp.attackMin = 35;
		wisp.attackMax = 40;
		wisp.defence = 10;
		wisp.cost = 500;
		wisp.creators = new BonusCreator[]
				{
						new BonusCreatorWisp(archerRange, new BonusAttackAlways(10, 0))
				};

		dire_wolf.attackMin = 60;
		dire_wolf.attackMax = 65;
		dire_wolf.defence = 15;
		dire_wolf.moveRadius = 5;
		dire_wolf.cost = 600;
		dire_wolf.creators = new BonusCreator[]
				{
						new BonusCreatorDireWolf(new BonusAttackAlways(-10, -10))
				};

		golem.attackMin = 60;
		golem.attackMax = 70;
		golem.defence = 30;
		golem.cost = 600;

		catapult.attackMin = 50;
		catapult.attackMax = 70;
		catapult.defence = 10;
		catapult.moveRadius = 3;
		catapult.cost = 700;
		catapult.attackRange = catapultRange;
		catapult.destroyingTypes = getCellTypes("BUILDING");
		catapult.attackRangeReverse = emptyRange;
		catapult.canDoTwoActionAfterOne = false;

		dragon.attackMin = 70;
		dragon.attackMax = 80;
		dragon.defence = 25;
		dragon.moveRadius = 6;
		dragon.cost = 1000;
		dragon.isFly = true;

		skeleton.attackMin = 40;
		skeleton.attackMax = 50;
		skeleton.defence = 2;
		skeleton.hasTombstone = false;

		crystal.attackMin = 0;
		crystal.attackMax = 0;
		crystal.defence = 15;
		crystal.attackRange = emptyRange;
		crystal.moveRadius = 3;
		crystal.addBonuses(new BonusMoveToCellType(mount, +3));

		king.specializations = new HashMap<>();
		king.addSpecialization(MyColor.BLUE, king_galamar);
		king.addSpecialization(MyColor.GREEN, king_valadorn);
		king.addSpecialization(MyColor.RED, king_demonlord);
		king.addSpecialization(MyColor.BLACK, king_saeth);
		king.attackMin = 55;
		king.attackMax = 65;
		king.defence = 20;
		king.cost = 400;
		king.repairTypes = getCellTypes("BUILDING_DESTROYING");
		king.captureTypes = getCellTypes("BUILDING", "CASTLE");
		king.isStatic = true;
		king.hasTombstone = false;
		king.addBonuses(new BonusCost(200));

		king_galamar.setProperties(king);
		king_valadorn.setProperties(king);
		king_demonlord.setProperties(king);
		king_saeth.setProperties(king);
	}

	public void createDefaultUnitType() {
		defaultUnitType.baseType = defaultUnitType;
		defaultUnitType.templateType = null;
		defaultUnitType.specializations = null;
		defaultUnitType.healthDefault = 100;
		defaultUnitType.attackMin = 50;
		defaultUnitType.attackMax = 55;
		defaultUnitType.defence = 5;
		defaultUnitType.moveRadius = 4;
		defaultUnitType.cost = 0;
		defaultUnitType.repairTypes = getCellTypes();
		defaultUnitType.captureTypes = getCellTypes();
		defaultUnitType.destroyingTypes = getCellTypes();
		defaultUnitType.attackRange = rules.getRange("DEFAULT");
		defaultUnitType.attackRangeReverse = rules.getRange("DEFAULT");
		defaultUnitType.raiseRange = rules.getRange("EMPTY");
		defaultUnitType.raiseType = null;
		defaultUnitType.isStatic = false;
		defaultUnitType.hasTombstone = true;
		defaultUnitType.canDoTwoActionAfterOne = true;
		defaultUnitType.isFly = false;
		defaultUnitType.bonuses = new Bonus[]
				{
						new BonusLevel(2, 2)
				};
		defaultUnitType.creators = new BonusCreator[0];
	}

	public CellType[] getCellTypes(String... names) {
		CellType[] types = new CellType[names.length];
		for (int i = 0; i < names.length; i++)
			types[i] = rules.getCellType(names[i]);
		return types;
	}

	public void createCellGroups() {
		defaultGroup.setTypes(mount, hill, two_trees, three_trees, plain, citadel_left, citadel_right, citadel_up, building_destroying);
		buildings.setTypes(castle, building);
		healing.setTypes(building, castle, camp, temple, citadel);
		water_way.setTypes(water_way_horizontal, water_way_vertical);

		water_way.setTypes(
				water_way_horizontal,
				water_way_vertical);

		waterGroup.setTypes(
				water,
				water_sparks);
	}

	public void createCellTypes() {
		createDefaultCellType();
		for (CellType type : rules.cellTypes)
			type.setProperties(rules.defaultCellType);
		for (CellGroup group : rules.cellGroups)
			group.baseType.setProperties(rules.defaultCellType);

		// default
		mount.steps = 3;
		mount.defense = 15;

		hill.steps = 2;
		hill.defense = 10;

		two_trees.steps = 2;
		two_trees.defense = 10;

		three_trees.steps = 2;
		three_trees.defense = 10;

		plain.defense = 5;

		citadel_left.defense = 10;
		citadel_right.defense = 10;
		citadel_up.defense = 10;

		// healing
		healing_group.defense = 15;
		healing_group.isHealing = true;
		camp.setProperties(healing_group);
		temple.setProperties(healing_group);
		citadel.setProperties(healing_group);
		citadel.struct = new StructCitadel(crystal, 3)
				.addConstraint(0, -1, citadel_left)
				.addConstraint(0, +1, citadel_right)
				.addConstraint(-1, 0, citadel_up);

		// buildings
		buildings_group.setProperties(healing_group);
		buildings_group.isCapturing = true;

		building.setProperties(buildings_group);
		building.earn = 30;
		building.destroyingType = building_destroying;

		building_destroying.defense = 15;
		building_destroying.repairType = building;

		castle.setProperties(buildings_group);
		castle.earn = 50;
		castle.buyTypes = getUnitTypes(
				"SOLDIER",
				"ARCHER",
				"ELEMENTAL",
				"SORCERESS",
				"WISP",
				"DIRE_WOLF",
				"GOLEM",
				"CATAPULT",
				"DRAGON");

		water_way_group.defense = 5;
		water_way.setBaseTypeToAll();

		water_group.steps = 3;
		waterGroup.setBaseTypeToAll();
		water.template = new CellTemplate(CellTemplateType.WATER, water,
				water_way_horizontal,
				water_way_vertical,
				water_sparks);

		way.template = new CellTemplate(CellTemplateType.WAY, way,
				water_way_horizontal,
				water_way_vertical,
				castle)
				.setFriendsUp(temple);
	}

	public UnitType[] getUnitTypes(String... names) {
		UnitType[] types = new UnitType[names.length];
		for (int i = 0; i < names.length; i++)
			types[i] = rules.getUnitType(names[i]);
		return types;
	}

	public void createDefaultCellType() {
		defaultCellType.isDefault = true;
		defaultCellType.baseType = defaultCellType;
		defaultCellType.steps = 1;
		defaultCellType.earn = 0;
		defaultCellType.defense = 0;
		defaultCellType.buyTypes = new UnitType[0];
		defaultCellType.isCapturing = false;
		defaultCellType.isHealing = false;
		defaultCellType.destroyingType = null;
		defaultCellType.repairType = null;
	}

	private void createDefaultGame() {
		// при изменении также проверять EditorThread.eraseDefaults()
		JsonObject object = new JsonObject();
		object.addProperty("currentTurn", 0);
		object.addProperty("allowedUnits", -1);
		rules.defaultGame = object;
	}

	private void createDefaultPlayers() {
		String[] keys = {
				"gold",
				"unitsLimit",
				"type"
		};

		Player player = new Player();
		Player computer = new Player();
		player.color = computer.color = MyColor.BLUE;
		player.team = computer.team = new Team(0);
		player.gold = computer.gold = 2000;
		player.unitsLimit = computer.unitsLimit = 20;

		player.type = PlayerType.PLAYER;
		computer.type = PlayerType.COMPUTER;

		rules.defaultPlayer = SerializableJsonHelper.leaveOnly(player.toJson(), keys);
		rules.defaultPlayerComputer = SerializableJsonHelper.leaveOnly(computer.toJson(), keys);
	}

	private void createMapEditorFrequencies() {
		plain.mapEditorFrequency = 100;
		hill.mapEditorFrequency = 50;
		three_trees.mapEditorFrequency = 50;
		two_trees.mapEditorFrequency = 50;
		mount.mapEditorFrequency = 20;
		building.mapEditorFrequency = 7;
		building_destroying.mapEditorFrequency = 7;
		castle.mapEditorFrequency = 5;
		camp.mapEditorFrequency = 2;
		temple.mapEditorFrequency = 2;
	}

}

