package client_form_input;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import shared_model.Batch;
import shared_model.Field;
import shared_model.Project;
import spell.SpellCorrector;
import spell.SuggestionBox;
import spell.ISpellCorrector.NoSimilarWordFoundException;
import client_facade.BatchState;
import client_indexer.GuiCell;

@SuppressWarnings("serial")
public class FormEntryPanel extends JSplitPane implements MouseListener, FocusListener, ListSelectionListener,
											ActionListener, BatchState.BatchStateListener {

	BatchState batchState;
	JList list;
	JPanel rightPanel;
	List<JTextField> fieldText;
	GridBagConstraints gbc;
	JPanel entryPanel;
	int prevListNum;
	int selectedRow;
	int selectedCol;
	boolean hadFocus;
	JMenuItem suggestions;
	JTextField selectedField;
	
	public FormEntryPanel(BatchState batchState) {
		this.batchState = batchState;
		batchState.addListener(this);
		prevListNum = -1;
		hadFocus = false;
		if (batchState.getProject().getID() >= 1) {
			update();
		}
	}
	public void update() {
		setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		setLeftComponent(new JScrollPane(setupList()));
		setRightComponent(new JScrollPane(setupRightPanel()));
		setDividerLocation(50);
		setResizeWeight(0.7);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JList setupList() {
		Object[] data;
		int rows = batchState.getProject().getRecordsPerImage();
		data = new Object[rows];
		for (int i = 0; i < rows; i++) {
			data[i] = i+1;
		}
		list = new JList(data);
		list.setMaximumSize(new Dimension(40, 200));
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		
		return list;
	}
	
	private JPanel setupRightPanel() {
		
		entryPanel = new JPanel(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipady = 10;
		fieldText = new ArrayList<JTextField>();
		
		List<Field> fields = batchState.getFields();
		for (int i = 0; i < fields.size(); i++) {
			setGBCLabel(i);
			entryPanel.add(new JLabel(fields.get(i).getName()), gbc);
			
			JTextField tempText = new JTextField();
			tempText.addMouseListener(this);
			fieldText.add(tempText);
			fieldText.get(i).addFocusListener(this);
			setGBCText(i);
			entryPanel.add(fieldText.get(i), gbc);
		}
		return entryPanel;
	}
	
	private void setGBCLabel(int field) {
		gbc.gridx = 0;
		gbc.gridy = field;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.ipadx = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
	}
	
	private void setGBCText(int field) {
		gbc.gridx = 1;
		gbc.gridy = field;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.ipadx = 20;
	}
	
	@Override
	public void valueChanged(GuiCell cell, String newValue) {
		if (list.getSelectedIndex() == cell.record) {
			JTextField currentField = fieldText.get(cell.field);
			currentField.setText(newValue);
			if (batchState.isMisspelledAt(cell))
				currentField.setBackground(new Color(245, 16, 16, 200));
			else
				currentField.setBackground(new Color(255, 255, 255, 255));
		}
	}
	@Override
	public void selectedCell(GuiCell newCell) {
		refillTextFields(newCell.record);
		list.setSelectedIndex(newCell.record);
		fieldText.get(newCell.field).grabFocus();
	}
	@Override
	public void projectChanged(Project project) {
		if (project.getID() >= 1) {
			update();
		}
		else {
			list.setVisible(false);
			entryPanel.setVisible(false);
			this.setVisible(false);
		}
	}
	@Override
	public void fieldsChanged(List<Field> fields) {
	}
	@Override
	public void batchChanged(Batch batch) {
	}
	@Override
	public void toggleHighlights(boolean highlightEnabled) {
	}
	@Override
	public void invert() {
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = list.getSelectedIndex();
		if (index != -1 && prevListNum != index) {
			prevListNum = index;
			int currentCol;
			if (batchState.getSelectedCell() != null)
				currentCol = batchState.getSelectedCell().field;
			else
				currentCol = 0;
			batchState.setSelectedCell(new GuiCell(index, currentCol));
		}
	}
	private void refillTextFields(int index) {
		for (int i = 0; i < fieldText.size(); i++) {
			String[][] values = batchState.getValues();
			fieldText.get(i).setText(values[index][i]);
			if (batchState.isMisspelledAt(new GuiCell(index, i)))
				fieldText.get(i).setBackground(new Color(245, 16, 16, 200));
			else
				fieldText.get(i).setBackground(new Color(255, 255, 255, 255));
		}
	}
	@Override
	public void focusGained(FocusEvent e) {
		if (!hadFocus) {
			hadFocus = true;
			//Find which text field gained focus
			for (int i = 0; i < fieldText.size(); i++) {
				if (e.getSource().equals(fieldText.get(i))) {
					int record = list.getSelectedIndex();
					batchState.setSelectedCell(new GuiCell(record, i));
				}
			}
		}
	}
	@Override
	public void focusLost(FocusEvent e) {
		hadFocus = false;
		//Find which text field lost focus
		for (int i = 0; i < fieldText.size(); i++) {
			if (e.getSource().equals(fieldText.get(i))) {
				String text = fieldText.get(i).getText();
				int record = list.getSelectedIndex();
				if (record != -1)
					batchState.setValues(new GuiCell(record, i), text);
				URL url;
				if (text.length() > 0) {
					try {
						text = text.toLowerCase();
						url = new URL(batchState.getFields().get(selectedCol).getKnownDataPath());
						SpellCorrector corrector = new SpellCorrector();
						corrector.useDictionary(url);
						Set<String> suggestedWords = corrector.suggestSimilarWords(text);
						if (suggestedWords.size() == 1 && suggestedWords.iterator().next().equals(text))
							batchState.setMisspelledWord(new GuiCell(record, i), false);
						else
							batchState.setMisspelledWord(new GuiCell(record, i), true);
					}
					catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (NoSimilarWordFoundException e1) {
						batchState.setMisspelledWord(new GuiCell(record, i), true);
					}
				}
			}
		}
	}
	@Override
	public void setPos(int x, int y) {
	}
	@Override
	public void setZoom(double zoom) {
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if ((e.getModifiers() & InputEvent.BUTTON3_MASK)
				== InputEvent.BUTTON3_MASK) {
			int col = 0;
			for (JTextField f : fieldText) {
				if (e.getSource() == f) {
					selectedField = f;
					break;
				}
				col++;
			}
			int row = list.getSelectedIndex();
			if (!batchState.isMisspelledAt(new GuiCell(row, col)))
				return;
			selectedRow = row;
			selectedCol = col;
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
			//Display the suggestions box
			String knownData = batchState.getFields().get(selectedCol).getKnownDataPath();
			if (knownData.length() < 2) {
				return;
			}
			URL url;
			try {
				url = new URL(batchState.getFields().get(selectedCol).getKnownDataPath());
				SpellCorrector corrector = new SpellCorrector();
				corrector.useDictionary(url);
				String value = selectedField.getText();
				Set<String> suggestedWords = corrector.suggestSimilarWords(value);
//				batchState.setSuggestedWords(suggestedWords);
				SuggestionBox sb = new SuggestionBox(batchState, suggestedWords, selectedRow,
													selectedCol);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (NoSimilarWordFoundException e1) {
				
			}
		}
	}
	
}
