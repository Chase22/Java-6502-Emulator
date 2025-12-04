package eater.ui.components.memory;

import eater.cpu.CpuState;
import eater.cpu.CpuStateListener;
import eater.cpu.CpuStatePublisher;

import javax.swing.*;
import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static eater.utils.CollectionUtils.toBoxedArray;

public class MemoryPanel extends JPanel implements CpuStateListener, Supplier<Byte[]> {
	private final MemoryTableModel memoryTableModel = new MemoryTableModel(this);
	private final JTable memoryTable = new MemoryTable(memoryTableModel);
	private final JPanel buttonPanel = new PaginatedTableButtons(memoryTableModel);
	private final Function<CpuState, byte[]> memoryExtractor;

	private CpuState cpuState;

	public MemoryPanel(Function<CpuState, byte[]> memoryExtractor, String title) {
		this.memoryExtractor = memoryExtractor;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.add(new JLabel(title.toUpperCase()));
		add(Box.createRigidArea(new Dimension(1, 10)));
		this.add(memoryTable);
		this.add(buttonPanel);
		CpuStatePublisher.addListener(this);

	}

	@Override
	public void onCpuStateChanged(CpuState state) {
		cpuState = state;
		memoryTable.invalidate();
	}

	@Override
	public Byte[] get() {
		return toBoxedArray(memoryExtractor.apply(cpuState));
	}
}
