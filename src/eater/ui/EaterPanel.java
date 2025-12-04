package eater.ui;

import eater.cpu.CpuState;
import eater.ui.components.memory.MemoryPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EaterPanel extends JFrame {
	public EaterPanel() {
		setTitle("6502 Eater");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setSize(800, 600);

		//setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setLayout(new FlowLayout());

		CPUPanel cpuPanel = new CPUPanel();
		add(cpuPanel);
		JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		tablePanel.add(new MemoryPanel(CpuState::ram, "ram"));
		tablePanel.add(new MemoryPanel(CpuState::rom, "rom"));

		add(tablePanel);

		UiUtils.setBackgroundColorRecursively(this, Color.BLUE);
		UiUtils.setForegroundColorRecursively(this, Color.WHITE);

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
