package ru.ancientempires;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import ru.ancientempires.actions.BuyStatus;
import ru.ancientempires.bridge.GameConverter;
import ru.ancientempires.campaign.CampaignEditor;
import ru.ancientempires.client.Client;
import ru.ancientempires.framework.FileLoader;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GameSaver;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.model.Cell;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.PlayerType;
import ru.ancientempires.rules.DefaultRules;
import ru.ancientempires.rules.Rules;

public class AllGamesConverter
{

	public enum Language
	{
		RU("_ru_RU"),
		EN("");

		public String suffix;

		Language(String suffix)
		{
			this.suffix = suffix;
		}
	}

	public class LocalizationFull
	{
		public FileLoader         loader;
		public LocalizationPart[] parts;	/*= Stream
												.of(Language.values())
												.map(LocalizationPart::new)
												.toArray(LocalizationPart[]::new);*/

		public LocalizationFull(String path)
		{
			parts = new LocalizationPart[Language.values().length];
			for (int i = 0; i < Language.values().length; i++)
				parts[i] = new LocalizationPart(Language.values()[i]);
			loader = AllGamesConverter.this.loader.getLoader(path);
		}

		public LocalizationFull save() throws IOException
		{
			for (LocalizationPart part : parts)
				part.save(loader);
			return this;
		}

		public LocalizationFull add(Enum e, String... strings)
		{
			return add(e.name(), strings);
		}

		public LocalizationFull add(String s, String... strings)
		{
			for (int i = 0; i < strings.length; i++)
				parts[i].strings.put(s, strings[i]);
			return this;
		}

		public LocalizationFull add(int iAE, String name)
		{
			return add(iAE, name, s -> s);
		}

		public LocalizationFull add(int iAE, String name, Function<String, String> mapper)
		{
			for (LocalizationPart part : parts)
				part.strings.put(name, mapper.apply(stringsAE[part.language.ordinal()][iAE]));
			return this;
		}

	}

	public class LocalizationPart
	{
		public Language language;
		public Map<String, String> strings = new LinkedHashMap<>();

		public LocalizationPart(Language language)
		{
			this.language = language;
		}

		public void save(FileLoader loader) throws IOException
		{
			JsonWriter writer = loader.getWriter("strings" + language.suffix + ".json");
			new Gson().toJson(strings, Map.class, writer);
			writer.close();
		}
	}

	public Rules      rules;
	public FileLoader loader;
	public String[][] stringsAE = new String[Language.values().length][];

	public void create() throws Exception
	{
		if (Client.client.gamesLoader.exists(""))
			Client.client.gamesLoader.deleteFolder("");
		rules = new DefaultRules().create();
		loader = Client.client.fileLoader;

		initLocalization();

		createGames("m", 8, "campaign", true);
		createGames("s", 12, "skirmish", false);

		createDefaultGame();
		createTestGame();
		createFolderNames();
		createStrings();
		createRulesLocalization();

		GamesFolder save = new GamesFolder("save", 0);
		save.isSave = true;
		save.save();
		GamesFolder user = new GamesFolder("user", 0);
		user.save();

		// Runtime.getRuntime().exec("cmd /c start \"\" /min quick");
		// Runtime.getRuntime().exec("./clear-saves.sh");
		System.out.println("All games converted!");
		// System.exit(0);
	}

	public void createTestGame() throws Exception
	{
		Game game = new Game(rules)
				.setSize(3, 3)
				.setNumberPlayers(3);

		new Cell(game, rules.getCellType("CASTLE"), 0, 0).setPlayer(0);
		new Cell(game, rules.getCellType("CASTLE"), 0, 1).setPlayer(1);
		new Cell(game, rules.getCellType("CASTLE"), 1, 0).setPlayer(2);

		//
		GamePath path = new GamePath(game, "test.0");
		path.isBaseGame = true;
		path.canChooseTeams = true;

		//new CampaignEditor(game).createTestGameCampaign();
		game.campaign.isDefault = true;

		GameSaver.createBaseGame(game);

		new LocalizationFull("games/" + path.path).add("name", "Тестовая игра", "").add("title", "1", "").save();

		// folder
		GamesFolder folder = new GamesFolder("test", 1);
		folder.save();

		new LocalizationFull("games/test/").add("name", "Тест", "Test").save();
	}

	public void createDefaultGame() throws Exception
	{
		Game game = new Game(rules);
		game.campaign.isDefault = true;
		new CampaignEditor(game).createDefaultGameCampaign();
		FileLoader loader = Client.client.gamesLoader.getLoader("defaultGame/");
		game.campaign.save(loader);

		LocalizationFull localization = new LocalizationFull("games/defaultGame/");
		localization.add(137, "target");
		localization.save();
	}

	public void createFolderNames() throws IOException
	{
		new LocalizationFull("games/campaign/").add("name", "Кампания", "Campaign").save();
		new LocalizationFull("games/skirmish/").add("name", "Схватка", "Skirmish").save();
		new LocalizationFull("games/save/").add("name", "Сохранения", "Saves").save();
		new LocalizationFull("games/user/").add("name", "Свои карты", "User maps").save();
	}

	public void createStrings() throws IOException
	{
		LocalizationFull localization = new LocalizationFull("");
		localization.add(MenuActions.PLAY, "Играть", "Play");
		localization.add(MenuActions.ONLINE, "Онлайн", "Online");
		localization.add(MenuActions.SETTINGS, "Настройки", "Settings");
		localization.add(MenuActions.MAP_EDITOR, "Редактор карт", "Map editor");
		localization.add(MenuActions.INSTRUCTIONS, "Инструкции", "Instructions");
		localization.add(MenuActions.AUTHORS, "Авторы", "Authors");

		localization.add(MenuActions.CAMPAIGN, "Кампания", "Campaign");
		localization.add(MenuActions.SKIRMISH, "Схватка", "Skirmish");
		localization.add(MenuActions.LOAD, "Загрузить", "Loading");
		localization.add(MenuActions.USER_MAPS, "Свои карты", "User maps");

		localization.add(MenuActions.CREATE_GAME, "Создать игру", "Create game");
		localization.add(MenuActions.EDIT_GAME, "Редактировать игру", "Edit game");

		localization.add(PlayerType.PLAYER, "Игрок", "Player");
		localization.add(PlayerType.COMPUTER, "Компьютер", "Computer");
		localization.add(PlayerType.NONE, "Нет", "None");

		localization.add(MyColor.GREY, "Серый", "Grey");
		localization.add(MyColor.RED, "Красный", "Red");
		localization.add(MyColor.GREEN, "Зелёный", "Green");
		localization.add(MyColor.BLUE, "Синий", "Blue");
		localization.add(MyColor.BLACK, "Чёрный", "Black");

		localization.add(BuyStatus.SUCCESS, "Успех!", "Success!");
		localization.add(BuyStatus.NO_GOLD, "Недостаточно золота", "Not enough gold");
		localization.add(BuyStatus.NO_PLACE, "Нет места для хода", "No cell for turn");
		localization.add(BuyStatus.UNIT_LIMIT_REACHED, "Достигнут димит войнов", "Units limit reached");

		localization.add(Strings.TEAM, "Команда", "Team");
		localization.add(Strings.GOLD, "Золото", "Money");
		localization.add(Strings.UNITS_LIMIT, "Лимит войнов", "Units limit");
		localization.add(Strings.FIGHT, "В бой!", "Fight!");
		localization.add(Strings.PROMOTION, "Повышение:\n%s", "Promotion:\n%s");

		localization.add(Strings.EDITOR_GAME_NAME, "Название уровня", "Level name");
		localization.add(Strings.EDITOR_GAME_HEIGHT, "Высота карты", "Map height");
		localization.add(Strings.EDITOR_GAME_WIDTH, "Ширина карты", "Map width");
		localization.add(Strings.EDITOR_GAME_NAME_TEMPLATE, "Игра %d", "Game %d");
		localization.save();
	}

	public void createRulesLocalization() throws IOException
	{
		LocalizationFull localization = new LocalizationFull("rules/");
		for (int i = 0; i < 12; i++)
		{
			String name = GameConverter.getUnitTypeName(i, 0);
			if (i == 9)
				name = "KING";
			for (int level = 0; level < 8; level += 2)
				if (i != 9 && i != 11 || level == 0)
					localization.add(139 + i * 4 + level / 2, name + "." + level);
			localization.add(184 + i, name + ".description");
		}
		for (int i = 0; i < 4; i++)
			localization.add(93 + i, GameConverter.getUnitTypeName(9, i) + ".0");
		localization.save();
	}

	public void createGames(String prefixAEM, int n, String prefixID, boolean isCampaign) throws Exception
	{
		for (int iMission = 0; iMission < n; iMission++)
		{
			FileInputStream input = new FileInputStream("/home/dima/projects/AE/maps/" + prefixAEM + iMission + ".aem");
			Game game = new GameConverter(rules).convertGame(input, iMission, isCampaign);

			GamePath path = new GamePath(game, prefixID + "." + iMission);
			path.baseGameID = path.gameID;
			path.nextGameID = isCampaign && iMission < n - 1 ? "campaign." + (iMission + 1) : null;
			path.isBaseGame = true;
			path.canChooseTeams = !isCampaign;

			if (isCampaign)
				new CampaignEditor(game).createCampaign(iMission);
			else
				game.campaign.isDefault = true;

			GameSaver.createBaseGame(game);

			LocalizationFull localization = new LocalizationFull("games/" + path.path);
			if (isCampaign)
				createLocalization(localization, iMission);
			else
				localization.add(101 + iMission, "name", s -> s.substring(3));
			localization.save();
		}

		GamesFolder folder = new GamesFolder(prefixID, n);
		folder.isCampaign = isCampaign;
		folder.save();
	}

	public void initLocalization() throws IOException
	{
		exctractStrings(Language.RU);
		exctractStrings(Language.EN);
	}

	public void exctractStrings(Language language) throws IOException
	{
		DataInputStream input = new DataInputStream(new FileInputStream("/home/dima/projects/Test/lang" + language.suffix + ".dat"));
		int n = input.readInt();
		String[] stringsPart = new String[n];
		stringsAE[language.ordinal()] = stringsPart;
		for (int i = 0; i < n; i++)
			stringsPart[i] = input.readUTF();
		if (printAllStrings)
			for (int i = 0; i < n; i++)
				System.out.println(i + "=\"" + stringsPart[i] + "\"");
		input.close();
	}

	public void createLocalization(LocalizationFull localization, int iMission)
	{
		localization.add(121 + iMission, "name");
		localization.add(113 + iMission, "title");
		localization.add(129 + iMission, "target");
		// localization.add(121 + iMission, "targetTitle");
		localization.add(72, "missionComplete");

		// В последней на два диалога больше, оказывается, 279 и 280 - не диалоги, а тосты
		int[] numberDialogs = new int[] {
				10,
				10,
				8,
				7,
				4,
				2,
				9,
				17
		};
		createLocalization(numberDialogs, 221, "dialog", localization, iMission);
		int[] numberIntros = new int[8];
		numberIntros[0] = 3;
		numberIntros[3] = 1;
		numberIntros[7] = 2;
		createLocalization(numberIntros, 215, "intro", localization, iMission);

		// System.out.println(221 + sum + i);
		// System.out.printf("%d %d %d\n", iMission, 221 + sum, 221 + sum + number - 1);
	}

	public void createLocalization(int[] numbers, int indexStart, String prefix, LocalizationFull localization, int iMission)
	{
		int number = numbers[iMission];
		int sum = Arrays.stream(numbers).limit(iMission).sum();
		for (int i = 0; i < number; i++)
			localization.add(indexStart + sum + i, prefix + "." + i);
	}

	private boolean printAllStrings = false;

}
