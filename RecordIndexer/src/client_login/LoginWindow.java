package client_login;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import client_facade.ClientException;
import client_facade.ClientFacade;
import shared_communication.ValidateUser_Result;

import javax.swing.*;

@SuppressWarnings("serial")
public class LoginWindow extends JDialog implements ActionListener, KeyListener {

	public interface Login {
		public void login(ValidateUser_Result result);
		public void error();
	}

	Login parent;
	JPanel rootPanel;
	JPanel textPanel;
	JPanel buttonPanel;
	JTextField usernameField;
	JPasswordField passwordField;
	JButton login;
	JButton exit;
	String focus;
	
	String host;
	int port;
	
	public LoginWindow(Login parent) {
		this.parent = parent;
		
		// Set the window's title
		this.setTitle("Record Indexer");
		
		// Specify what should happen when the user clicks the window's close icon
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		createPanels();
		this.setSize(330,140);
		this.setLocationRelativeTo(null);
	}

	
	private void createPanels() {
        
		rootPanel = new JPanel();
		
		rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
		rootPanel.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(2,2,10,20));
		textPanel.add(new JLabel("Username", JLabel.CENTER));
		usernameField = new JTextField();
		usernameField.addKeyListener(this);
		textPanel.add(usernameField);
		textPanel.add(new JLabel("Password", JLabel.CENTER));
		passwordField = new JPasswordField();
		passwordField.addKeyListener(this);
		textPanel.add(passwordField);
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(Box.createHorizontalGlue());
		login = new JButton("login");
		login.addActionListener(this);
		login.addKeyListener(this);
		buttons.add(login);
		buttons.add(Box.createHorizontalGlue());
		exit = new JButton("exit"); 
		exit.addActionListener(this);
		buttons.add(exit);
		buttons.add(Box.createHorizontalGlue());
		
		rootPanel.add(textPanel);
		rootPanel.add(Box.createRigidArea(new Dimension(0,10)));
		rootPanel.add(buttons);
		rootPanel.add(Box.createRigidArea(new Dimension(0,50)));
		this.add(rootPanel);
	
		// Size the window according to the preferred sizes and layout of its subcomponents
		this.pack();

	}
	
	private void tryLogin() throws ClientException {
		String username = usernameField.getText();
		char[] pw = passwordField.getPassword();
		String password = new String(pw);
		parent.login(ClientFacade.validateUser(username, password));
	}

	/*
	 * 
	 * ACTION EVENTS BELOW
	 * 
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == login) {
			try {
				tryLogin();
			} catch (ClientException e1) {
				parent.error();
			}
		}
		else {
			this.dispose();
		}
	}
	
	public void resetParams() {
		usernameField.setText("");
		passwordField.setText("");
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			try {
				tryLogin();
			} catch (ClientException e1) {
				parent.error();
			}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
