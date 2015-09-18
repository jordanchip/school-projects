package client.communication;

import org.json.simple.JSONObject;

/**
 * 
 * @author Steven Pulsipher
 * Communication between client and server
 */
public class ClientCommunicator {

	/**
	 * creates a new ClientCommunicator
	 */
	public ClientCommunicator(){
	}
	
	/**
	 * Sends to the server
	 * @param o the JSON Object that is going to be sent
	 * @pre JSON Object is valid, and contains a location to be sent as well as "Get" or "Post"
	 * @post Response from the server will be given
	 */
	public JSONObject send(JSONObject o){
		return null;
	}
}
