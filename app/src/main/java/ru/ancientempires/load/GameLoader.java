package ru.ancientempires.load;

public class GameLoader
{
	/*
	public GamePath path;
	public Rules    rules;

	public GameLoader(GamePath path, Rules rules)
	{
		this.path = path;
		this.rules = rules;
	}
	
	public Game load2(boolean loadCampaign) throws Exception
	{
		//game = new GameSnapshotLoader(path, rules).load(loadCampaign);
		FileLoader loader = path.getLoader();
		Scanner scannerAll = loader.getScanner(GamePath.LAST_SNAPSHOT);
		String all = scannerAll.nextLine();
		scannerAll.close();

		Scanner scanner = new Scanner(all);
		int numberActions = scanner.nextInt();
		int sizeActions = scanner.nextInt();
		String gameString = scanner.nextLine();
		scanner.close();

		JsonObject gameJson = (JsonObject) new JsonParser().parse(gameString);
		Game game = new Game(rules);
		game.path = path;
		game.fromJson(gameJson);

		if (loadCampaign)
		{
			Client.client.defaultGameLoader.loadLocalization();
			if (loader.exists("strings.json"))
				loader.loadLocalization();

			//game.campaign.load((game.campaign.isDefault = !loader.exists("campaign.json")) ? Client.client.defaultGameLoader : loader);
			game.campaign.isDefault = !loader.exists("campaign.json");
			game.campaign.load(game.campaign.isDefault ? Client.client.defaultGameLoader : loader);
		}
		if (sizeActions < path.sizeActions)
		{
			FileInputStream fis = loader.openFIS(GamePath.ACTIONS);
			fis.getChannel().position(sizeActions);
			DataInputStream dis = new DataInputStream(fis);
			for (int i = numberActions; i < path.numberActions; i++)
			{
				Action action = Action.loadNew(dis);
				action.checkBase(game);
				action.performQuickBase(game);
			}
		}
		return game;
	}
	*/
	
}
