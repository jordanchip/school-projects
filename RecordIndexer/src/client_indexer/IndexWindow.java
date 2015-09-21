package client_indexer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.util.List;

import javax.swing.*;

import shared_model.Batch;
import shared_model.Field;
import shared_model.Project;
import client_facade.BatchState;
import client_facade.ClientException;
import client_facade.ClientFacade;
import client_facade.UserState;

@SuppressWarnings("serial")
public class IndexWindow extends JFrame implements ActionListener, WindowListener, WindowStateListener,
												BatchState.BatchStateListener {

	public interface Context {
		public void logout();
		public void exit();
	}
	
	Context context;
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem downloadFile;
	JMenuItem logout;
	JMenuItem exit;
	JPanel rootPanel;
	ImageViewer imageViewer;
	MenuPanel menuPanel;
	BatchState batchState;
	BottomSplitPane bottomPane;
	JSplitPane rootSplitPane;
	static ClientFacade facade;
	GridBagConstraints gbc;
	
	public IndexWindow(Context c) {
		context = c;
		batchState = new BatchState();
		this.setTitle("Record Indexer");
		
		// Specify what should happen when the user clicks the window's close icon
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setMinimumSize(new Dimension(540,600));
		this.addWindowListener(this);
		this.addWindowStateListener(this);
		
		UserState batchStateRetriever = new UserState();
		batchState = batchStateRetriever.getStateForUser(ClientFacade.user.getUsername());
		createPanels();
		batchState.addListener(this);
		
		if (batchState.getProject().getID() > 0) {
			batchState.notifyAllListeners();
		}
		if (batchState.getWindowWidth() == 0) {
			this.setSize(1200,900);
			this.setLocationRelativeTo(null);
		}
		else {
			this.setSize(batchState.getWindowWidth(), batchState.getWindowHeight());
			this.setLocation(batchState.getWindowX(), batchState.getWindowY());
		}
	}
	
	private void createPanels() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		menuBar = setupMenu();
		rootPanel.add(menuBar, gbc);
		
		menuPanel = new MenuPanel(batchState);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.ipady = 10;
		rootPanel.add(menuPanel, gbc);
		
		imageViewer = new ImageViewer(batchState);
		
		bottomPane = new BottomSplitPane(batchState);
		rootSplitPane = new JSplitPane();
		rootSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		rootSplitPane.setTopComponent(imageViewer);
		rootSplitPane.setBottomComponent(bottomPane);
		if (batchState.getHorizontalDividerSplit() == 0)
			rootSplitPane.setDividerLocation(550);
		else
			rootSplitPane.setDividerLocation(batchState.getHorizontalDividerSplit());
		rootSplitPane.setResizeWeight(0.7);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipady = 80;
		gbc.weighty = 0.5;
		rootPanel.add(rootSplitPane, gbc);
		
		this.add(rootPanel);
	}
	
	private JMenuBar setupMenu() {
		menuBar = new JMenuBar();
		menuBar.setMinimumSize(new Dimension(0,20));
		
		downloadFile = new JMenuItem("Download File");
		if (ClientFacade.user.getCurrentAssignedBatch() > 0)
			downloadFile.setEnabled(false);
		downloadFile.addActionListener(this);
		logout = new JMenuItem("Logout");
		logout.addActionListener(this);
		exit = new JMenuItem("Exit");
		exit.addActionListener(this);
		
		menu = new JMenu("File");
		menu.add(downloadFile);
		menu.add(logout);
		menu.add(exit);
		
		menuBar.add(menu);
		return menuBar;
	}
	private void handleDownloadFile() {
		if (batchState.getProject().getID() > 0) {
			String message = "You are already assigned to a batch.  Please finish your\n" +
					"current batch before downloading another";
			JOptionPane.showMessageDialog(null, message, "", JOptionPane.ERROR_MESSAGE);
		}
		else {
			try {
				@SuppressWarnings("unused")
				DownloadFileDialogBox db = new DownloadFileDialogBox(ClientFacade.getProjects(), batchState);
			} catch (ClientException e1) {
				e1.printStackTrace();
			}
		}
	}
	private void save() {
		UserState saver = new UserState();
		batchState.setWindowX(this.getX());
		batchState.setWindowY(this.getY());
		batchState.setWindowWidth(this.getWidth());
		batchState.setWindowHeight(this.getHeight());
		if (rootSplitPane != null)
			batchState.setHorizontalDividerSplit(rootSplitPane.getDividerLocation());
		if (bottomPane != null)
			batchState.setVerticalDividerSplit(bottomPane.getDividerLocation());
		saver.save(batchState, ClientFacade.user.getUsername());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == downloadFile) {
			handleDownloadFile();
		} 
		else if (e.getSource() == logout) {
			save();
			context.logout();
			this.dispose();
		}
		else if (e.getSource() == exit) {
			save();
			context.exit();
		}
	}
    @Override
    public void windowClosing(WindowEvent e)
    {
        save();
        e.getWindow().dispose();
    }	
    @Override
	public void projectChanged(Project project) {
		if (project.getID() > 0)
			downloadFile.setEnabled(false);
		else
			downloadFile.setEnabled(true);
		this.repaint();
	}
	@Override
	public void valueChanged(GuiCell cell, String newValue) {
	}
	@Override
	public void selectedCell(GuiCell newCell) {
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
    /*
     * Following are unimplemented WindowEvent Actions
     */
	@Override
	public void windowOpened(WindowEvent e) {
	}
	@Override
	public void windowClosed(WindowEvent e) {
	}
	@Override
	public void windowIconified(WindowEvent e) {
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
	}
	@Override
	public void windowActivated(WindowEvent e) {
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	/*
	 * END
	 */

	@Override
	public void windowStateChanged(WindowEvent e) {
		batchState.setWindowX(e.getWindow().getX());
		batchState.setWindowY(e.getWindow().getY());
		batchState.setWindowWidth(e.getWindow().getWidth());
		batchState.setWindowHeight(e.getWindow().getHeight());
	}
	
}
