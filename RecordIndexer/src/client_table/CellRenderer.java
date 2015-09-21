package client_table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import client_facade.BatchState;
import client_indexer.GuiCell;

@SuppressWarnings("serial")
public class CellRenderer extends JTextField implements TableCellRenderer {

	private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	private Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

	private int prevSelectedRow = 0;
	private int prevSelectedCol = 0;
	private BatchState batchState;
	
	public CellRenderer(BatchState batchState) {
		this.batchState = batchState;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		if (isSelected) {
			if (prevSelectedRow != row || prevSelectedCol != column) {
				prevSelectedRow = row;
				prevSelectedCol = column;
				if (column > 0) {
					batchState.setSelectedCell(new GuiCell(row, column-1));
				}
			}
			this.setBorder(selectedBorder);
			//If selected, fill with a transparent blue background
			this.setBackground(new Color(12, 43, 245, 100));
		}
		
		else {
			this.setBorder(unselectedBorder);
			//If not selected, make invisible)
			this.setBackground(new Color(12, 43, 245, 0));
		}
		if (column > 0) {
			if (value instanceof String) {
				String temp = (String)value;
				if (temp.length() > 0 && batchState.isMisspelledAt(new GuiCell(row, column-1)))
					this.setBackground(new Color(245, 16, 16, 200));
			}
			else
				this.setBackground(new Color(245, 16, 16, 200));
		}
		if (value instanceof String)
			this.setText((String)value);
		else if (value instanceof Integer)
			this.setText(Integer.toString((Integer)value));
		
		return this;
	}

}
