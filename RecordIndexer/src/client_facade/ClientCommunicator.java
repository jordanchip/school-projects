package client_facade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared_communication.*;


/**
 * A communicator that allows the client to communicate with the server through various methods such
 * as validateUser, getProjects, etc. 
 * Each of the methods has a class for its input parameters as 
 * well as its output, to reduce the number of parameters 
 * being sent from the client to the server.
 *  
 * For example, the validateUser method has a validateUser_Input 
 * class to store all the input parameters, as well as a 
 * validateUser_Result class to store all of the output.
 * @author jchip
 *
 */
public class ClientCommunicator {
	
	private String SERVER_HOST;
	private int SERVER_PORT;
	private String URL_PREFIX;
	private static final String HTTP_POST = "POST";

	private XStream xmlStream;

	public ClientCommunicator() {
		xmlStream = new XStream(new DomDriver());
	}
	public ClientCommunicator(String host, int port) {
		xmlStream = new XStream(new DomDriver());
		SERVER_HOST = host;
		SERVER_PORT = port;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public void setHostAndPort(String host, int port) {
		SERVER_HOST = host;
		SERVER_PORT = port;
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public String getURLPrefix() {
		return URL_PREFIX;
	}
	
	/**
	 * Checks if a given user exists in the database with a 
	 * given username and password.
	 * @param params A user containing a username and password to be validated
	 * @return a ValidateUser_Result object which contains the user if successfully validated. 
	 * Otherwise, returns A blank user with an id of -1.
	 * @throws ClientException Happens if any errors occurred while processing the database request.
	 */
	public ValidateUser_Result validateUser(ValidateUser_Input params) throws ClientException {
		return (ValidateUser_Result)doPost("/ValidateUser", params);
	}
	
	/**
	 * Retrieves all projects available in the database.
	 * @param params An object containing a username and password to be validated
	 * @return a GetProjects_Result object which contains all projects in the database.
	 * @throws ClientException Happens if any errors occurred while processing the database request.
	 */
	public GetProjects_Result getProjects(GetProjects_Input params) throws ClientException {
		return (GetProjects_Result)doPost("/GetProjects", params);
	}
	
	/**
	 * Retrieves one sample image for a given project.
	 * @param params A GetSampleImage_Input object which contains the project for which a batch is desired.
	 * @return A GetSampleImage_Result object which contains the sample batch object.
	 * @throws ClientException Happens if any errors occurred while processing the database request.
	 */
	public GetSampleImage_Result getSampleImage(GetSampleImage_Input params) throws ClientException {
		return (GetSampleImage_Result)doPost("/GetSampleImage", params);
		//TODO
	}
	
	/**
	 * Retrieves an image for a project and assigns it to a user.  This will only 
	 * assign a batch to a user if there is an existing batch available.  If the user 
	 * already has a batch assigned to them, this operation will fail.
	 * @param params A DownloadBatch_Input object which contains the project for which a batch is desired.
	 * @return A DownloadBatch_Result object which contains the retrieved batch object.
	 * @throws ClientException 
	 */
	public DownloadBatch_Result downloadBatch(DownloadBatch_Input params) throws ClientException {
		return (DownloadBatch_Result)doPost("/DownloadBatch", params);
	}
	
	/**
	 * Sends all indexed fields to the database for storage, then flags the user as 
	 * having no currently assigned batches.  Increments the number of records indexed 
	 * for that user.
	 * @param params A SubmitBatch_Input object which contains the fields to be saved to the database.
	 * @throws ClientException 
	 */
	public SubmitBatch_Result submitBatch(SubmitBatch_Input params) throws ClientException {
		return (SubmitBatch_Result)doPost("/SubmitBatch", params);
	}
	
	/**
	 * Gets all the fields for a given project. If no project is specified, returns 
	 * information about all of the fields for all projects in the database.
	 * @param params The batch which contains the desired fields.
	 * @return A getFields_Result object which contains all the found fields
	 * @throws ClientException 
	 */
	public GetFields_Result getFields(GetFields_Input params) throws ClientException {
		return (GetFields_Result)doPost("/GetFields", params);
	}
	
	/**
	 * Searches the database for specified strings
	 * @param params A Search_Input object which contains all the strings to be searched for.
	 * @return A Search_Result object containing lists of all found matches.
	 * @throws ClientException 
	 */
	public Search_Result search(Search_Input params) throws ClientException {
		return (Search_Result)doPost("/Search", params);
	}
	
	public DownloadFile_Result downloadFile(DownloadFile_Input params) throws ClientException {
		return (DownloadFile_Result)doPost("/" + params.getPath(), params);
	}
	
	private Object doPost(String urlPath, Object postData) throws ClientException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			}
			else {
				throw new ClientException(String.format("doPost failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			throw new ClientException(String.format("doPost failed: %s", e.getMessage()), e);
		}
	}
	
	
	
}
