package ru.ancientempires;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.campaign.CampaignImmediately;
import ru.ancientempires.client.Client;
import ru.ancientempires.ii.II;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.model.Game;

public class Main
{

	private static void test2() throws Exception
	{
		FileOutputStream fos = new FileOutputStream("test.dat");
		for (int i = 0; i < 7; i++)
			fos.write(i);
		fos.close();

		fos = new FileOutputStream("test.dat");
		fos.getChannel().position(3);
		fos.write(77);
		fos.close();
		System.exit(0);
	}

	private static void test() throws Exception
	{
		Game game = Client.client.startGame("skirmish.5");
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();

		Client.client.stopGame();
		System.exit(0);
	}

	public static void main(String[] args) throws Exception
	{
		//test2();

		//new File(System.getenv("appdata") + "\\Ancient Empires\\");
		Client client = new Client(new DesktopClientHelper());

		//new RulesSaver(client.fileLoader, new DefaultRules().create()).save("rules/rules.json");
		new AllGamesConverter().create();
		//System.exit(0);
		
		client.loadPart1();
		client.loadPart2();

		//new Swing("save.0");
		//new Swing("skirmish.5");
		testFull();
		//test();
		//testII("skirmish.5");
	}

	public static void testFull() throws Exception
	{
		List<GamesFolder> gamesFolders = Arrays.asList(Client.client.campaign, Client.client.skirmish);
		for (GamesFolder gamesFolder : gamesFolders)
			for (GamePath path : gamesFolder.games)
				testLoadGame(path.gameID, true);
		for (GamesFolder gamesFolder : gamesFolders)
			for (GamePath path : gamesFolder.games)
				testII(path.gameID);
		System.exit(0);
	}

	public static void testII(String gameID) throws Exception
	{
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();

		for (int i = 0; !game.players[0].units.isEmpty() && i < 100; i++)
		{
			//System.out.println("i = " + i);
			new ActionGameEndTurn().perform(game);
			game.campaign.update();
			new II(game.rules).turnFull(game);
			game.campaign.update();
		}
		if (!game.players[0].units.isEmpty())
			System.out.println(String.format("!!! units not empty %s (%d)", gameID, game.players[0].units.size()));

		game.saver.finishSave();
		testLoadGame(game.path.gameID, true);
	}

	public static void testLoadGame(String gameID, boolean reload) throws Exception
	{
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();
		game.saver.finishSave();

		if (reload)
			testLoadGame(game.path.gameID, false);
	}

	private static void captureScreen(String path) throws Exception
	{
		File folder = new File("/home/dima/AndroidStudioProjects/missions/" + path);
		folder.mkdirs();
		for (File file : folder.listFiles())
			file.delete();
		Robot robot = new Robot();
		Rectangle rect = new Rectangle(103, 137, 242, 322);

		long start = System.currentTimeMillis();
		long prev = start;
		System.out.println("Main.captureScreen()");
		for (int i = 0; i < 50000; ++i)
		{
			long curr = System.currentTimeMillis();
			BufferedImage image = robot.createScreenCapture(rect);
			ImageIO.write(image, "png", new File(folder, String.format("%d_%d_%d.png", i, curr - prev, (curr - start) * 15 / 1000)));
			prev = curr;
		}
		System.out.println("OK");
		System.exit(0);
	}
	
}
