package client;

import java.awt.EventQueue;

import client_indexer.IndexWindow;
import client_indexer.IndexWindow.Context;
import client_login.LoginWindow;
import client_login.ValidateUserWindow;
import client_login.LoginWindow.Login;
import shared_communication.ValidateUser_Result;
import client_facade.ClientFacade;

public class Client implements Login, Context {

	ValidateUserWindow userWindow;
	LoginWindow loginWindow;
	IndexWindow indexWindow;
	static ClientFacade facade;
	
	
	public static void main(String[] args) {
		
		final String host;
		String hostTemp = args[0];
		if (hostTemp.equals(""))
			host = "localhost";
		else
			host = hostTemp;
		final int port;
		String portTemp = args[1];
		if (portTemp.equals(""))
			port = 8080;
		else
			port = Integer.parseInt(portTemp);
		
		ClientFacade.initiateClientCommunicator();
		ClientFacade.setHostAndPort(host, port);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Client();
			}
		});

	}
	
	public Client() {
		
		loginWindow = new LoginWindow(login);		
		loginWindow.setResizable(false);
		// Make the frame window visible
		loginWindow.setVisible(true);
	}
	
	private LoginWindow.Login login = new LoginWindow.Login() {

		@Override
		public void login(ValidateUser_Result result) {
			userWindow = new ValidateUserWindow();
			loginWindow.resetParams();
			if (result.getUser().getID() != -1) {
				userWindow.showWelcomeMessage(result);
				loginWindow.setVisible(false);
				indexWindow = new IndexWindow(context);
				indexWindow.setVisible(true);
			} else {
				userWindow.showInvalidUserMessage();
			}
		}

		@Override
		public void error() {
			userWindow = new ValidateUserWindow();
			loginWindow.resetParams();
			userWindow.showServerErrorMessage();
		}
	};
	
	private IndexWindow.Context context = new IndexWindow.Context() {
		@Override
		public void logout() {
			loginWindow.resetParams();
			loginWindow.setVisible(true);
		}
		@Override
		public void exit() {
			System.exit(0);
		}
	};

	@Override
	public void logout() {
	}
	@Override
	public void login(ValidateUser_Result result) {
	}
	@Override
	public void error() {
	}
	@Override
	public void exit() {
	}

}
