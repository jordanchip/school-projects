package client_login;

import javax.swing.JOptionPane;

import shared_communication.ValidateUser_Result;

@SuppressWarnings("serial")
public class ValidateUserWindow extends JOptionPane {
	
	public ValidateUserWindow() {
		
		this.setSize(500,400);
		this.setLocation(150,100);
			
	}
	
	public void showWelcomeMessage(ValidateUser_Result result) {
		String validMessage = "Welcome, " + result.getUser().getFirstName() + " " + result.getUser().getLastName() +
				"!\nYou have indexed: " + result.getUser().getIndexedRecords() + " records.";
		showMessageDialog(null, validMessage, "Welcome!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showInvalidUserMessage() {
		String invalidUserMessage = "That's an invalid username/password.  Please try again.";
		showMessageDialog(null, invalidUserMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void showServerErrorMessage() {
		String serverErrorMessage = "We're sorry, but the server is not working as this moment.  Please " +
				"try again later.";
		showMessageDialog(null, serverErrorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

}
