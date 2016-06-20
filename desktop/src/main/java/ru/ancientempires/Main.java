package ru.ancientempires;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import ru.ancientempires.actions.ActionGameEndTurn;
import ru.ancientempires.activities.BaseGameActivity;
import ru.ancientempires.campaign.CampaignImmediately;
import ru.ancientempires.client.Client;
import ru.ancientempires.draws.BaseDrawMain;
import ru.ancientempires.ii.II;
import ru.ancientempires.load.GamePath;
import ru.ancientempires.load.GamesFolder;
import ru.ancientempires.model.Game;
import ru.ancientempires.swing.Swing;

public class Main
{

	private static void test2() throws Exception
	{
		Game game = Client.client.startGame("skirmish.5");
		BaseGameActivity.activity = new BaseGameActivity();
		BaseGameActivity.activity.game = game;
		BaseDrawMain mainBase = new BaseDrawMain()
		{
			@Override
			public void setVisibleMapSize()
			{}
		};
		mainBase.iMax = game.h;
		mainBase.jMax = game.w;

		int h = game.h * 24;
		int w = game.w * 24;
		Bitmap bitmap = Bitmap.createBitmap(w, h, null);
		Canvas canvas = new Canvas(bitmap);

		mainBase.cells.draw(canvas);
		mainBase.cellsDual.draw(canvas);
		mainBase.unitsDead.draw(canvas);
		mainBase.units.draw(canvas);

		ImageIO.write(bitmap.image, "png", new File("app/src/main/res/drawable/example-game.png"));
	}

	private static void test() throws Exception
	{
		Game game = Client.client.startGame("campaign.1");
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();

		for (int i = 0; !game.players[0].units.isEmpty(); i++)
		{
			//System.out.println("i = " + i);
			new ActionGameEndTurn().perform(game);
			game.campaign.update();
			new II(game.rules).turnFull(game);
			game.campaign.update();
		}

		System.out.println(game.toJsonPretty());

		game.saver.finishSave();
		testLoadGame(game.path.gameID, true);
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

		//new CampaignEditor(Client.client.startGame("campaign.7")).convert(7);
		//testLoadGame("campaign.7", true);

		new Swing("test.0");
		//testFull();
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
