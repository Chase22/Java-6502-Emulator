package ui.components.memory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.font.FontRenderContext;

public class MemoryTable extends JTable {
	public MemoryTable(TableModel dm) {
		super(dm);
		setShowGrid(false);
		autoResizeMode = JTable.AUTO_RESIZE_OFF;
		//TODO the width here is static, not great when we want to change fontsize. Ideally measure the content dynamically
		for (int i = 0; i < columnModel.getColumnCount(); i++) {
			columnModel.getColumn(i).setMaxWidth(9999); // Otherwise the column collapses into the minimum size
			columnModel.getColumn(i).setPreferredWidth(30);
		}
		columnModel.getColumn(0).setPreferredWidth(60);
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		if (column == 0) {
			return new IndexColumnRenderer();
		}
		return new HexColumnRenderer(2);
	}

	public static class HexColumnRenderer extends DefaultTableCellRenderer {
		private final String format;

		public HexColumnRenderer(int length) {
			this.format = "%0" + length + "X";
			setHorizontalAlignment(SwingConstants.CENTER);
			//setBorder(BorderFactory.createCompoundBorder(null, BorderFactory.createEmptyBorder(10,10,10,10)));
		}

		@Override
		protected void setValue(Object value) {
			setText(String.format(format, value));

			/*Rectangle2D metrics = getFontMetrics(getFont()).getStringBounds(getText(), getGraphics());
			setSize(metrics.getBounds().getSize());*/
		}
	}

	public static class IndexColumnRenderer extends HexColumnRenderer {
		public IndexColumnRenderer() {
			super(4);
			setHorizontalAlignment(SwingConstants.RIGHT);
		}
	}
}
