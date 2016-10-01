package ru.ancientempires.draws.campaign;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import ru.ancientempires.activities.GameActivity;
import ru.ancientempires.campaign.scripts.Script;

public class SimpleDialog {

	public static void create(GameActivity activity, String text, Script script) {
		if (1 == 1) {
			script.finish();
			return;
		}

		JTextArea textArea = new JTextArea(text);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.getCaret().setVisible(false);
		Font font = new Font("Verdana", Font.PLAIN, 20);
		textArea.setFont(font);
		textArea.setMargin(new Insets(20, 20, 20, 20));

		JFrame frame = new JFrame("Toast");
		frame.setUndecorated(true);
		frame.add(textArea);
		frame.setSize(350, 400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();
				script.finish();
				activity.getView().needUpdateCampaign = true;
			}
		});
	}
}
