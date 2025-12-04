package eater.ui.components;

import eater.cpu.CpuState;
import eater.cpu.CpuStateListener;
import eater.cpu.CpuStatePublisher;

import javax.swing.*;
import java.util.function.Function;

public class CpuStateLabel extends JLabel implements CpuStateListener {
	private final Function<CpuState, String> onNewCpuState;

	public CpuStateLabel(Function<CpuState, String> onNewCpuState) {
		CpuStatePublisher.addListener(this);
		this.onNewCpuState = onNewCpuState;
	}

	@Override
	public void onCpuStateChanged(CpuState state) {
		setText(this.onNewCpuState.apply(state));
	}
}

