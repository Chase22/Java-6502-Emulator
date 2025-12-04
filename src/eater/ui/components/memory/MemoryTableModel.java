package eater.ui.components.memory;

import java.util.function.Supplier;

public class MemoryTableModel extends PaginatedTableModel {

	private final Supplier<Byte[]> memoryDataSupplier;

	public MemoryTableModel(Supplier<Byte[]> memoryDataSupplier) {
		super(32);
		this.memoryDataSupplier = memoryDataSupplier;
	}

	@Override
	public int getColumnCount() {
		return 9;
	}

	@Override
	public int getTotalRowCount() {
		return (memoryDataSupplier.get().length-1)/8;
	}

	@Override
	public Object getValueAtInternal(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return rowIndex*8;
		} else {
			int arrayIndex = rowIndex * 8 + columnIndex;
			Byte[] data = memoryDataSupplier.get();

			if (arrayIndex > data.length) {
				return null;
			} else {
				return data[arrayIndex];
			}
		}
	}
}
