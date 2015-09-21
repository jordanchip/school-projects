package shared_communication;

/**
 * Contains all the input for the getSampleImage method. 
 * Contains a username and password to validate, as well as 
 * a project ID for which a sample image is desired.
 * @author jchip
 *
 */
public class GetSampleImage_Input {

	private String username;
	private String password;
	private int projectID;
	
	public GetSampleImage_Input(String[] values) {
		if (values.length == 3) {
			username = values[0];
			password = values[1];
			if (!values[2].isEmpty())
				projectID = Integer.parseInt(values[2]);
			else
				projectID = 0;
		}
	}
	public GetSampleImage_Input() {
		
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
