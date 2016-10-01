package ru.ancientempires.swing;

import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import ru.ancientempires.client.Client;
import ru.ancientempires.framework.MyAssert;
import ru.ancientempires.model.Game;
import ru.ancientempires.model.PlayerType;

public class Swing {

	public Swing(String gameID) throws Exception {
		Game game = Client.client.startGame(gameID);
		System.out.println(game.path.gameID);
		JFrame frame = new JFrame("Ancient Empires");

		// end turn
		JTextField text = new JTextField("Конец хода");
		text.setEditable(false);
		text.getCaret().setVisible(false);
		Font font = new Font("Verdana", Font.PLAIN, 30);
		text.setFont(font);
		text.setMargin(new Insets(10, 10, 10, 10));
		text.setHorizontalAlignment(SwingConstants.CENTER);
		frame.add("North", text);

		GameComponent component = new GameComponent(game, frame);
		frame.add(component);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		//frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				try {
					Client.client.stopGame();
					System.exit(0);
				} catch (Exception e) {
					MyAssert.a(false);
					e.printStackTrace();
				}
			}
		});

		text.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				component.runOnGameThread(() -> component.inputMain.endTurn(true));
			}
		});

		Robot robot = new Robot();
		File folder = new File("/home/dima/projects/missions/1");
		boolean write = false;
		folder.mkdirs();
		if (write)
			for (File file : folder.listFiles())
				file.delete();
		Rectangle rectangle = new Rectangle((1600 - 600) / 2, (900 - 700) / 2, 600 + 50, 700 + 50);
		int i = 0;

		game.campaign.start();
		if (component.drawMain.isActiveGame()) {
			MyAssert.a(component.inputMain.game.currentPlayer.type == PlayerType.PLAYER);
			component.inputMain.beginTurn();
		}
		while (true) {
			//System.out.println(DrawCameraMove.delta + " " + DrawUnitMove.framesForCell);
			component.paintImmediately(0, 0, component.w, component.h);
			if (!write)
				Thread.sleep(33);
			else if (i % 2 == 0) {
				Thread.sleep(10);
				BufferedImage image = robot.createScreenCapture(rectangle);
				ImageIO.write(image, "png", new File(folder, i + ".png"));
			}
			++i;
		}
	}

}
