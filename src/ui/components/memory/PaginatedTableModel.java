package ui.components.memory;

import utils.ObservableProperty;

import javax.swing.table.AbstractTableModel;

public abstract class PaginatedTableModel extends AbstractTableModel {
	public final ObservableProperty<Integer> pageProperty = new ObservableProperty<>(0);
	private final int rowsPerPage;

	protected PaginatedTableModel(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getPage() {
		return pageProperty.getValue();
	}

	public void next() {
		setPage(getPage()+1);
	}

	public void previous() {
		setPage(getPage()-1);
	}

	public boolean hasNext() {
		return getPage() < getPageCount()-1;
	}

	public boolean hasPrevious() {
		return getPage() > 0;
	}

	public void setPage(int page) {
		if (page < 0) {
			throw new IndexOutOfBoundsException("Page index cannot be negative");
		}
		int maxIndex = getPageCount() - 1;
		if (page > maxIndex) {
			throw new IndexOutOfBoundsException(String.format("Page index %d is larger than max index: %d", page, maxIndex));
		}
		this.pageProperty.setValue(page);
		fireTableDataChanged();
	}

	public abstract int getTotalRowCount();

	public int getPageCount() {
		return getTotalRowCount()/rowsPerPage;
	}

	@Override
	public int getRowCount() {
		return rowsPerPage;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getValueAt(getPage(), rowIndex, columnIndex);
	}

	public Object getValueAt(int page, int rowIndex, int columnIndex) {
		return getValueAtInternal(page*rowsPerPage+rowIndex, columnIndex);
	};

	public abstract Object getValueAtInternal(int rowIndex, int columnIndex);
}
