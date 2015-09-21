package shared_communication;

/**
 * Contains the input to the getFields method. 
 * Contains a username and password to validate, as 
 * well as a project ID to get Fields for.  If no 
 * project ID is indicated, will get all the fields 
 * across all projects.
 * @author jchip
 *
 */
public class GetFields_Input {

	private String username;
	private String password;
	private int projectID;
	
	public GetFields_Input(String[] values) {
		if (values.length == 3) {
			username = values[0];
			password = values[1];
			if (!values[2].isEmpty())
				projectID = Integer.parseInt(values[2]);
			else
				projectID = -1;
		}
	}
	public GetFields_Input() {
		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
}
