package client_help;

import javax.swing.JButton;
import javax.swing.JTabbedPane;

import client_facade.BatchState;
import client_indexer.ImageViewer;

@SuppressWarnings("serial")
public class HelpTabPanel extends JTabbedPane {

	public HelpTabPanel(BatchState batchState) {
		FieldHelpPanel helpPanel = new FieldHelpPanel(batchState);
		this.addTab("Help", helpPanel);
		JButton temp2 = new JButton("WIP.");
		//ImageViewer imageNavigator = new ImageViewer(batchState);
		this.addTab("Image Navigator", temp2);
	}
}
