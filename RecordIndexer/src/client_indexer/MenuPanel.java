package client_indexer;

import java.awt.Dimension;

import client_facade.BatchState;
import client_facade.ClientException;
import client_facade.ClientFacade;
import client_facade.UserState;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements ActionListener {
	
	JButton zoomIn;
	JButton zoomOut;
	JButton invert;
	JButton toggle;
	JButton save;
	JButton submit;
	BatchState batchState;
	
	public MenuPanel(BatchState batchState) {
		this.batchState = batchState;
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		setupButtons();
	}
	
	private void setupButtons() {
		this.add(Box.createRigidArea(new Dimension(2,0)));
		
		zoomIn = new JButton("Zoom In");
		zoomIn.addActionListener(this);
		this.add(zoomIn);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		
		zoomOut = new JButton("Zoom Out");
		zoomOut.addActionListener(this);
		this.add(zoomOut);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		
		invert = new JButton("Invert");
		invert.addActionListener(this);
		this.add(invert);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		
		toggle = new JButton("Toggle");
		toggle.addActionListener(this);
		this.add(toggle);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		
		save = new JButton("Save");
		save.addActionListener(this);
		this.add(save);
		this.add(Box.createRigidArea(new Dimension(5,0)));
		
		submit = new JButton("Submit");
		submit.addActionListener(this);
		this.add(submit);
		this.add(Box.createHorizontalGlue());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == zoomIn) {
			batchState.setZoom(batchState.getZoom()*1.1);
		}
		else if (e.getSource() == zoomOut) {
			batchState.setZoom(batchState.getZoom()*0.9);
		}
		else if (e.getSource() == invert) {
			batchState.invert();
		}
		else if (e.getSource() == toggle) {
			batchState.toggleHighlights();
		}
		else if (e.getSource() == save) {
			UserState saver = new UserState();
			saver.save(batchState, ClientFacade.user.getUsername());
		}
		else if (e.getSource() == submit) {
			if (batchState.getBatch().getID() <= 0) {
				String message = "You must download an image before you can submit one.";
				JOptionPane.showMessageDialog(null, message, "Sample Image", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				int batchID = batchState.getBatch().getID();
				try {
					ClientFacade.submit(batchID, batchState.getValues()).toString();
					batchState.resetProjectStatus();
				} catch (ClientException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	
}
