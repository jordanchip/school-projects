package client_indexer;

import java.awt.Dimension;

import javax.swing.JSplitPane;

import client_facade.BatchState;
import client_form_input.InputTabPanel;
import client_help.HelpTabPanel;

@SuppressWarnings("serial")
public class BottomSplitPane extends JSplitPane {

	public BottomSplitPane(BatchState batchState) {
		InputTabPanel inputPanel = new InputTabPanel(batchState);
		HelpTabPanel helpPanel = new HelpTabPanel(batchState);
		
		this.setLeftComponent(inputPanel);
		this.setRightComponent(helpPanel);
		
		Dimension d = new Dimension(190, 80);
		helpPanel.setMinimumSize(d);
		inputPanel.setMinimumSize(d);
		if (batchState.getVerticalDividerSplit() == 0)
			this.setDividerLocation(550);
		else
			this.setDividerLocation(batchState.getVerticalDividerSplit());
		this.setResizeWeight(0.5);
	}
}
