package ui;

import javax.swing.*;

public class CPUPanel extends JPanel {
	public CPUPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(new ClockPanel());
		add(new CpuRegistersPanel());
	}
}

