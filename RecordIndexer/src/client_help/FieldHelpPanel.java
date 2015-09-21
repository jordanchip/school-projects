package client_help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import shared_model.Batch;
import shared_model.Field;
import shared_model.Project;
import client_facade.BatchState;
import client_facade.BatchState.BatchStateListener;
import client_indexer.GuiCell;

@SuppressWarnings("serial")
public class FieldHelpPanel extends JPanel implements BatchStateListener {

	private JEditorPane htmlViewer;
	BatchState batchState;
    private JTextArea htmlTextArea;
    int currentField;
	
	public FieldHelpPanel(BatchState batchState) {
		this.batchState = batchState;
		batchState.addListener(this);
		this.setLayout(new BorderLayout());
		
		currentField = -1;
		htmlViewer = new JEditorPane();
		htmlViewer.setOpaque(true);
		htmlViewer.setPreferredSize(new Dimension(500, 600));
		htmlViewer.setEditable(false);
		htmlViewer.addHyperlinkListener(hyperlinkListener); 
		htmlViewer.addPropertyChangeListener(propertyChangeListener);
		htmlTextArea = new JTextArea();
        htmlTextArea.setPreferredSize(new Dimension(500, 600));
        htmlViewer.setContentType("text/html");
        
        JScrollPane htmlScrollPane = new JScrollPane(htmlViewer);
		htmlScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(htmlScrollPane, BorderLayout.CENTER);
	}
	
	private HyperlinkListener hyperlinkListener = new HyperlinkListener() {

		public void hyperlinkUpdate(HyperlinkEvent e) {

			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				String url = e.getURL().toString();
		        loadPage(url);
			}
		}
	};
	
	private PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			htmlTextArea.setText(htmlViewer.getText());
		}      	
    };

    private void loadPage(String url) {

        try {
        	if (url.length() > 1) {
        		htmlViewer.setPage(url);
        		htmlViewer.setVisible(true);
        	}
        	else
        		htmlViewer.setVisible(false);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
	@Override
	public void valueChanged(GuiCell cell, String newValue) {
	}
	@Override
	public void selectedCell(GuiCell newCell) {
		if (newCell.field != currentField) {
			currentField = newCell.field;
			Field currentField = batchState.getFields().get(newCell.field);
			loadPage(currentField.getHelpHTMLPath());
			this.setVisible(true);
		}
	}
	@Override
	public void projectChanged(Project project) {
		if (project.getID() < 1)
			htmlViewer.setVisible(false);
		else
			htmlViewer.setVisible(true);
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
	public void setPos(int x, int y) {
	}
	@Override
	public void setZoom(double zoom) {

	}
}
