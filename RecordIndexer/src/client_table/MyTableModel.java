package client_table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import client_facade.BatchState;
import client_indexer.GuiCell;
import shared_model.Batch;
import shared_model.Field;
import shared_model.Project;

@SuppressWarnings("serial")
public class MyTableModel extends AbstractTableModel implements BatchState.BatchStateListener{

	List<Field> fields;
	Project project;
	BatchState batchState;
	TableEntryPanel parent;
	
	public MyTableModel(BatchState batchState) {
		this.batchState = batchState;
		batchState.addListener(this);
	}
	public void addParentListener(TableEntryPanel parent) {
		this.parent = parent;
	}
	
	@Override
	public int getRowCount() {
		if (project != null)
			return project.getRecordsPerImage();
		return 0;
	}

	@Override
	public int getColumnCount() {
		if (fields != null)
			return fields.size() + 1;
		return 0;
	}
	
	@Override
	public String getColumnName(int column) {
		if (fields == null || fields.size() == 0)
			return null;
		String result = null;

		if (column >= 0 && column < getColumnCount()) {

			if (column == 0)
				result = "Record No.";
			else
				result = fields.get(column-1).getName();

		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= 0 && rowIndex < project.getRecordsPerImage() &&
				columnIndex >= 0 && columnIndex < fields.size() + 1) {
			if (columnIndex == 0)
				return rowIndex + 1;
			
			String[][] values = batchState.getValues();
			return values[rowIndex][columnIndex-1];
		}
		return null;
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		
		if (row >= 0 && row < getRowCount() && column >= 1
				&& column < getColumnCount()) {

			String cellValue = (String)value;
			String[][] values = batchState.getValues();
			values[row][column-1] = cellValue;
			
			this.fireTableCellUpdated(row, column);
			
		} else {
			throw new IndexOutOfBoundsException();
		}		
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0)
			return false;
		return true;
	}

	@Override
	public void valueChanged(GuiCell cell, String newValue) {
		this.fireTableCellUpdated(cell.record, cell.field);
	}

	@Override
	public void selectedCell(GuiCell newCell) {
		parent.selectedCell(newCell);
	}
	@Override
	public void projectChanged(Project project) {
		this.project = project;
		this.fireTableStructureChanged();
	}
	@Override
	public void fieldsChanged(List<Field> fields) {
		this.fields = fields;
	}
	@Override
	public void batchChanged(Batch batch) {
	}
	@Override
	public void toggleHighlights(boolean highlightEnabled) {
		parent.highlightEnabled(highlightEnabled);
	}
	@Override
	public void invert() {
	}
	@Override
	public void setPos(int x, int y) {
	
	}
	@Override
	public void setZoom(double zoom) {
	}
	
}
