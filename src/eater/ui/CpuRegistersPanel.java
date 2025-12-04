package eater.ui;

import eater.cpu.CpuState;
import eater.ui.components.CpuStateLabelFactory;

import javax.swing.*;

public class CpuRegistersPanel extends JPanel {
	public CpuRegistersPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(CpuStateLabelFactory.forByte("A:", CpuState::a));
		add(CpuStateLabelFactory.forByte("X:", CpuState::x));
		add(CpuStateLabelFactory.forByte("Y:", CpuState::y));
		add(CpuStateLabelFactory.forByte("Stack Pointer:", CpuState::stackPointer));
		add(CpuStateLabelFactory.forShort("Program Counter:", CpuState::programCounter));
		add(CpuStateLabelFactory.forFlags());
		add(CpuStateLabelFactory.forShort("Address Absolute:", CpuState::addressAbsolute));
		add(CpuStateLabelFactory.forShort("Address Relative:", CpuState::addressRelative));
		add(CpuStateLabelFactory.forOpCode());
	}
}
