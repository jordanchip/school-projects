package client_form_input;

import javax.swing.JTabbedPane;

import client_facade.BatchState;
import client_table.TableEntryPanel;

@SuppressWarnings("serial")
public class InputTabPanel extends JTabbedPane {

	public InputTabPanel(BatchState batchState) {
		TableEntryPanel tablePanel = new TableEntryPanel(batchState);
		this.addTab("Table Entry", tablePanel);
		FormEntryPanel formPanel = new FormEntryPanel(batchState);
		this.addTab("Form Entry", formPanel);
	}
}
