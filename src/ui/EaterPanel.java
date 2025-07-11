package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EaterPanel extends JFrame {
	public EaterPanel() {
		setTitle("6502 Eater");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(800, 600);

		getContentPane().setBackground(Color.BLUE);
		getContentPane().setForeground(Color.WHITE);

		add(new CPUPanel());

		Font font;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT,this.getClass().getClassLoader().getResourceAsStream("courbd.ttf")).deriveFont(20f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(font);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
			return;
		}
		UiUtils.setFontRecursive(this.getContentPane(), font);
	}
}
