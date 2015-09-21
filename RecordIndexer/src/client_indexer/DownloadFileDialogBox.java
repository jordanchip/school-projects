package client_indexer;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.imageio.*;
import javax.swing.*;

import client_facade.BatchState;
import client_facade.ClientException;
import client_facade.ClientFacade;
import shared_communication.DownloadBatch_Result;
import shared_communication.GetProjects_Result;
import shared_model.Project;

@SuppressWarnings("serial")
public class DownloadFileDialogBox extends JDialog implements ActionListener {
	
	List<Project> projects;
	JPanel rootPanel;
	JButton viewSample;
	JButton downloadFile;
	JButton exit;
	JComboBox<Object> projectOptions;
	private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
	
	String selectedProject;
	BatchState batchState;
	
	public DownloadFileDialogBox(GetProjects_Result result, BatchState batchState) {
		projects = result.getProjects();
		this.batchState = batchState;
		selectedProject = projects.get(0).getName();
		
		this.setPreferredSize(new Dimension(370, 130));
		this.setSize(490, 130);
		this.setModal(true);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		createDialogBox();
	}
	
	private void createDialogBox() {
		rootPanel = new JPanel();
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		
		rootPanel.add(Box.createRigidArea(new Dimension(0, 15)));
		
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.X_AXIS));
		fieldPanel.add(Box.createHorizontalGlue());
		fieldPanel.add(new JLabel("Project:"));
		fieldPanel.add(Box.createRigidArea(new Dimension(5,0)));
		
		projectOptions = new JComboBox<Object>(getProjectNames());
		projectOptions.setMaximumSize(new Dimension(20,30));
		projectOptions.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXX");
		projectOptions.addActionListener(this);
		fieldPanel.add(projectOptions);
		fieldPanel.add(Box.createRigidArea(new Dimension(6,0)));
		
		viewSample = new JButton("View Sample Image");
		viewSample.addActionListener(this);
		fieldPanel.add(viewSample);
		fieldPanel.add(Box.createHorizontalGlue());
		
		rootPanel.add(fieldPanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0, 13)));
		
		JPanel buttonPanel = new JPanel();
		downloadFile = new JButton("Download File");
		downloadFile.addActionListener(this);
		exit = new JButton("Exit");
		exit.addActionListener(this);
		
		buttonPanel.add(downloadFile);
		buttonPanel.add(exit);
		
		rootPanel.add(buttonPanel);
		
		this.add(rootPanel);
		
		this.setVisible(true);
	}
	
	private String[] getProjectNames() {
		String[] projectNames = new String[projects.size()];
		for (int i = 0; i < projects.size(); i++) {
			projectNames[i] = projects.get(i).getName();
		}
		return projectNames;
	}
	
	
	private Project findProject(String selectedProject) {
		for (Project p : projects) {
			if (p.getName() == selectedProject)
				return p;
		}
		return null;
	}
	
	private Image loadImage(URL imageURL) {
		try {
			Image image = ImageIO.read(imageURL).getScaledInstance(640, 450, Image.SCALE_SMOOTH);
			return image;
		} catch (IOException e) {
			return NULL_IMAGE;
		}
	}

	private void showSampleImage() {
		Project project = findProject(selectedProject);
		String url = null;
		try {
			url = ClientFacade.getSampleImage(project).getBatch().getPath();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		try {
			URL imageURL = new URL(url);
			Image image = loadImage(imageURL);
			JLabel picLabel = new JLabel(new ImageIcon(image));
			JOptionPane.showMessageDialog(null, picLabel, "Sample Image", JOptionPane.INFORMATION_MESSAGE);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	private DownloadBatch_Result downloadBatch() {
		Project project = findProject(selectedProject);
		DownloadBatch_Result result = null;
		try {
			result = ClientFacade.downloadBatch(project);
			System.out.println(result.toString());
			if (result.isHasFailed()) {
				return null;
			}
			batchState.setFields(result.getFields());
			batchState.setBatch(result.getBatch());
			batchState.setProject(result.getProject());
			return result;
		} catch (ClientException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 
	 * ACTIONS PERFORMED HERE
	 * 
	 */
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == projectOptions) {
			JComboBox<?> cb = (JComboBox<?>)e.getSource();
			selectedProject = (String)cb.getSelectedItem();
		}
		else if (e.getSource() == viewSample) {
			showSampleImage();
		}
		else if (e.getSource() == downloadFile) {
			if (downloadBatch() == null)
			{
				String error = "There are no more batches to download for this project.  Please select another.";
				JOptionPane.showMessageDialog(null, error, "Sample Image", JOptionPane.ERROR_MESSAGE);
			}
			else
				this.dispose();
		}
		else if (e.getSource() == exit) {
			this.dispose();
		}
	}
	
}
