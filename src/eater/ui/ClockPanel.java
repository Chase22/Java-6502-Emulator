package eater.ui;

import eater.ui.components.CpuStateLabel;

import javax.swing.*;

public class ClockPanel extends JPanel {
	public ClockPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(new CpuStateLabel(state -> "Clocks:" + state.clocks()));
		add(new CpuStateLabel(state -> "Speed: %.1f Hz %s".formatted(state.speed(), state.slowerClock() ? "(Slow)" : "")));
		add(new CpuStateLabel(state -> "Cycles:" + state.cycles()));
	}
}
