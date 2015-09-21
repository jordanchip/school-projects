package spell;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;

import client_facade.BatchState;
import client_indexer.GuiCell;

@SuppressWarnings("serial")
public class SuggestionBox extends JDialog implements ActionListener {

	BatchState batchState;
	JList suggestions;
	JButton cancel;
	JButton useSuggestion;
	Set<String> words;
	int row;
	int col;
	
	public SuggestionBox(BatchState batchState, Set<String> words, int row, int col) {
		this.batchState = batchState;
		this.row = row;
		this.col = col;
		this.words = words;
		this.setLayout(new GridBagLayout());
		setupPanels();

		this.setResizable(false);
		this.setSize(new Dimension(300, 250));
		this.setLocationRelativeTo(null);
		this.setVisible(false);
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setVisible(true);
	}
	
	private void setupPanels() {
		GridBagConstraints gbc = new GridBagConstraints();
		if (words != null) {
			suggestions = new JList(makeObjectFromSet(words));
		}
		else
			suggestions = new JList(new Object[0]);
		suggestions.setSelectionMode(JList.VERTICAL);
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		
		useSuggestion = new JButton("Use Suggestion");
		useSuggestion.addActionListener(this);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 0.5;
		gbc.ipady = 100;
		gbc.ipadx = 30;
		gbc.insets = new Insets(5,20,5,20);
		gbc.fill = GridBagConstraints.BOTH;
		this.add(new JScrollPane(suggestions),gbc);
		
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.ipady = 30;
		gbc.insets = new Insets(5, 10, 5, 10);
		this.add(cancel,gbc);
		
		gbc.gridx = 1;
		this.add(useSuggestion,gbc);
		
		if (words.size() == 0)
			useSuggestion.setEnabled(false);
		else
			suggestions.setSelectedIndex(0);
		this.pack();
		this.setVisible(true);
	}
	private Object[] makeObjectFromSet(Set<String> words) {
		Object[] wordObjectArray = new Object[words.size()];
		int i = 0;
		for (String w : words) {
			wordObjectArray[i] = w;
			i++;
		}
		return wordObjectArray;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancel)
			this.dispose();
		else if (e.getSource() == useSuggestion) {
			if (suggestions.getSelectedIndex() != -1) {
				Object temp = suggestions.getSelectedValue();
				if (temp instanceof String) {
					String newWord = (String)temp;
					GuiCell cell = new GuiCell(row, col);
					batchState.setValues(cell, newWord);
					batchState.setMisspelledWord(cell, false);
					this.dispose();
				}
			}
		}
	}
	
}
