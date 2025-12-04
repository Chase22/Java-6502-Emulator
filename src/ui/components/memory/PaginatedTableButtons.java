package ui.components.memory;

import ui.EaterButton;
import utils.PropertyChangedListener;

import javax.swing.*;

public class PaginatedTableButtons extends JPanel implements PropertyChangedListener<Integer> {
	private final PaginatedTableModel paginatedTableModel;

	private final EaterButton firstPageButton;
	private final EaterButton previousPageButton;
	private final EaterButton nextPageButton;
	private final EaterButton lastPageButton;
	private final JLabel pageLabel = new JLabel("0");

	public PaginatedTableButtons(PaginatedTableModel paginatedTableModel) {
		this.paginatedTableModel = paginatedTableModel;
		paginatedTableModel.pageProperty.registerListener(this);
		firstPageButton = new EaterButton("<==", e -> paginatedTableModel.setPage(0));
		previousPageButton = new EaterButton("<--", e -> paginatedTableModel.previous());
		nextPageButton = new EaterButton("-->", e -> paginatedTableModel.next());
		lastPageButton = new EaterButton("==>", e -> paginatedTableModel.setPage(paginatedTableModel.getPageCount()-1));
		add(firstPageButton);
		add(previousPageButton);
		add(pageLabel);
		add(nextPageButton);
		add(lastPageButton);

		previousPageButton.setEnabled(false);
	}

	@Override
	public void propertyChanged(Integer newValue, Integer oldValue) {
		previousPageButton.setEnabled(paginatedTableModel.hasPrevious());
		nextPageButton.setEnabled(paginatedTableModel.hasNext());
		pageLabel.setText(newValue.toString());
	}
}
