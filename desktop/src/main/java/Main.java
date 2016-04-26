import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ru.ancientempires.WindowsClientHelper;
import ru.ancientempires.action.ActionGameEndTurn;
import ru.ancientempires.campaign.CampaignImmediately;
import ru.ancientempires.client.Client;
import ru.ancientempires.ii.II;
import ru.ancientempires.model.Game;
import ru.ancientempires.swing.Swing;

public class Main
{

	private static void test() throws Exception
	{
		System.exit(0);
	}

	public static void main(String[] args) throws Exception
	{
		//test();

		//new File(System.getenv("appdata") + "\\Ancient Empires\\");
		Client client = new Client(new WindowsClientHelper());
		
		//new RulesSaver(client.fileLoader, new DefaultRules().create()).save("rules/rules.json");
		new AllGamesConverter().create();
		//System.exit(0);
		
		client.loadPart1();
		client.loadPart2();
		
		//testLoadGame("campaign.6", true);
		new Swing("campaign.6");
	}

	private static void captureScreen(String path) throws Exception
	{
		File folder = new File("/home/dima/projects/missions/" + path);
		folder.mkdirs();
		Robot robot = new Robot();
		Rectangle rect = new Rectangle(600, 100, 300, 350);
		
		// Thread.sleep(1000);
		System.out.println("Main.captureScreen()");
		for (int i = 0; i < 100; ++i)
		{
			BufferedImage image = robot.createScreenCapture(rect);
			ImageIO.write(image, "png", new File(folder, "" + i));
			// Thread.sleep(1000);
		}
		System.out.println("OK");
		System.exit(0);
	}

	public static void testLoadGame(String gameID, boolean reload) throws Exception
	{
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();
		game.saver.finishSave();
		if (reload)
			testLoadGame(game.path.gameID, false);
		System.exit(0);
	}
	
	public static void testII(String gameID) throws Exception
	{
		Game game = Client.client.startGame(gameID);
		game.campaign.iDrawCampaign = new CampaignImmediately(game);
		game.campaign.start();
		
		while (!game.players[1].units.isEmpty())
		{
			// game.players[1].units.get(0).health = 0;
			// new UnitHelper(game).checkDied(game.players[1].units.get(0));
			new ActionGameEndTurn().perform(game);
			game.campaign.update();
			new II().turnFull(game);
			game.campaign.update();
		}
		
		game.saver.finishSave();
		testLoadGame(game.path.gameID, false);
	}
	
}
