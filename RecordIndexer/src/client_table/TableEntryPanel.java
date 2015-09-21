package client_table;

import java.awt.BorderLayout;

import client_facade.BatchState;
import client_indexer.GuiCell;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import shared_model.Field;
import shared_model.Project;
import spell.ISpellCorrector.NoSimilarWordFoundException;
import spell.SpellCorrector;
import spell.SuggestionBox;

@SuppressWarnings("serial")
public class TableEntryPanel extends JPanel implements TableModelListener, MouseListener,
														ActionListener {

	JTable table;
	JScrollPane scrollPane;
	BatchState batchState;
	List<Field> fields;
	Project project;
	MyTableModel tableModel;
	JMenuItem suggestions;
	int selectedCellRow;
	int selectedCellCol;
	
	public TableEntryPanel(BatchState batchState) {
		this.batchState = batchState;
		initializeTable();
	}
	
	public void initializeTable() {

		tableModel = new MyTableModel(batchState);
		table = new JTable(tableModel);
		tableModel.addParentListener(this);
		tableModel.addTableModelListener(this);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		table.getTableHeader().setReorderingAllowed(false);
		table.addMouseListener(this);
		table.getModel().addTableModelListener(new TableModelListener() 
		{
			@Override
			public void tableChanged(TableModelEvent e) {
				repaint();
			}
		}
		);
		
		resetTable();
		
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
							JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(90, 50));
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void changedCell(int row, int column) {
		batchState.setSelectedCell(new GuiCell(row, column));
	}

	
	public void highlightEnabled(boolean highlightEnabled) {
		table.setCellSelectionEnabled(highlightEnabled);
	}

	public void selectedCell(GuiCell cell) {
		table.changeSelection(cell.record, cell.field+1, false, false);
	}

	public void resetTable() {
		table.revalidate();
		table.setDefaultRenderer(Object.class, new CellRenderer(batchState));
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow();
		int col = e.getColumn();
		Object temp = table.getModel().getValueAt(row, col);
		if (temp instanceof String) {
			String value = (String)temp;
			value = value.toLowerCase();
			if (value.length() > 0) {
				try {
					String knownData = batchState.getFields().get(col-1).getKnownDataPath();
					if (knownData.length() < 2) {
						resetTable();
						return;
					}
					URL url = new URL(batchState.getFields().get(col-1).getKnownDataPath());
					SpellCorrector corrector = new SpellCorrector();
					corrector.useDictionary(url);
					Set<String> suggestedWords = corrector.suggestSimilarWords(value);
					//batchState.setSuggestedWords(new GuiCell(row, col-1), suggestedWords);
					if (suggestedWords.size() == 1 && suggestedWords.iterator().next().equals(value))
						batchState.setMisspelledWord(new GuiCell(row, col-1), false);
					else {
						batchState.setMisspelledWord(new GuiCell(row, col-1), true);
					}
				} catch (MalformedURLException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSimilarWordFoundException e1) {
					batchState.setMisspelledWord(new GuiCell(e.getFirstRow(),e.getColumn()-1), true);
				}
				
			}
		}
		resetTable();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK)
				== InputEvent.BUTTON3_MASK) {
			int row = table.rowAtPoint(e.getPoint());
			int col = table.columnAtPoint(e.getPoint());
			if (col == 0)
				return;
			if (!batchState.isMisspelledAt(new GuiCell(row, col-1)))
				return;
			selectedCellRow = row;
			selectedCellCol = col-1;
			JPopupMenu popup = new JPopupMenu();
			suggestions = new JMenuItem("See Suggestions");
			suggestions.addActionListener(this);
			popup.add(suggestions);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == suggestions) {
			String knownData = batchState.getFields().get(selectedCellCol).getKnownDataPath();
			if (knownData.length() < 2) {
				resetTable();
				return;
			}
			URL url;
			try {
				url = new URL(batchState.getFields().get(selectedCellCol).getKnownDataPath());
				SpellCorrector corrector = new SpellCorrector();
				corrector.useDictionary(url);
				Object temp = table.getModel().getValueAt(selectedCellRow, selectedCellCol+1);
				String value;
				if (temp instanceof Integer)
					value = Integer.toString((Integer)temp);
				else
					value = (String)temp;
				Set<String> suggestedWords = corrector.suggestSimilarWords(value);
//				batchState.setSuggestedWords(suggestedWords);
				SuggestionBox sb = new SuggestionBox(batchState, suggestedWords, selectedCellRow,
													selectedCellCol);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoSimilarWordFoundException e1) {
				
			}
		}
	}
}
