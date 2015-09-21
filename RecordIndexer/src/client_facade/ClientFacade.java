package client_facade;
import shared_communication.*;
import shared_model.*;

public class ClientFacade {
	
	private static String host;
	private static int port;
	private static BatchState batchState;
	public static User user;
	private static String username;
	private static String password;
	private static ClientCommunicator comm;
	
	public ClientFacade() {
		comm = new ClientCommunicator();
	}
	
	public static void initiateClientCommunicator() {
		comm = new ClientCommunicator();
	}
	public static void setHostAndPort(String host1, int port1) {
		host = host1;
		port = port1;
		comm.setHostAndPort(host1, port1);
	}
	
	public static BatchState getBatchState() {
		return batchState;
	}

	public static ValidateUser_Result validateUser(String username2, String password2) throws ClientException {

		username = username2;
		password = password2;
		
		ValidateUser_Input params = new ValidateUser_Input();
		params.setUsername(username);
		params.setPassword(password);
		
		ValidateUser_Result result = comm.validateUser(params);
		user = result.getUser();
		return result;
	}
	
	public static GetProjects_Result getProjects() throws ClientException {
		
		GetProjects_Input params = new GetProjects_Input();
		params.setUsername(username);
		params.setPassword(password);
		
		return comm.getProjects(params);
	}
	
	public static GetSampleImage_Result getSampleImage(Project project) throws ClientException {
		
		GetSampleImage_Input params = new GetSampleImage_Input();
		params.setUsername(username);
		params.setPassword(password);
		params.setProjectID(project.getID());
		
		GetSampleImage_Result result = comm.getSampleImage(params);
		String appender = "http://" + host + ":" + Integer.toString(port) +
						"/database/Records/" + result.getBatch().getPath();
		result.getBatch().setPath(appender);
		return result;
	}
	
	public static DownloadBatch_Result downloadBatch(Project project) throws ClientException {
		
		DownloadBatch_Input params = new DownloadBatch_Input();
		params.setUsername(username);
		params.setPassword(password);
		params.setProjectID(project.getID());
		
		DownloadBatch_Result result = comm.downloadBatch(params);
		String appender = "http://" + host + ":" + Integer.toString(port) +
				"/database/Records/" + result.getBatch().getPath();
		result.getBatch().setPath(appender);
		String appender2 = "http://" + host + ":" + Integer.toString(port) +
				"/database/Records/";
		for (Field f : result.getFields()) {
			if (f.getHelpHTMLPath().length() > 1)
				f.setHelpHTMLPath(appender2 + f.getHelpHTMLPath());
			if (f.getKnownDataPath().length() > 1)
				f.setKnownDataPath(appender2 + f.getKnownDataPath());
		}
		
		return result;
	}
	
public static SubmitBatch_Result submit(int batchID, String[][] cells) throws ClientException {
		
		SubmitBatch_Input params = new SubmitBatch_Input();
		params.setUsername(username);
		params.setPassword(password);
		params.setBatchID(batchID);
		params.setCells(cells);
		
		return comm.submitBatch(params);
	}
	
}
