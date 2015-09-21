package shared_communication;

/**
 * Contains the inputs to the download batch method. 
 * Contains a username and password to validate, as well as a project 
 * ID for which a batch is requested.
 * @author jchip
 *
 */
public class DownloadBatch_Input {

	private String username;
	private String password;
	private int projectID;
	
	public DownloadBatch_Input(String[] values) {
		/*
		 * Setup this object with the given String[]
		 * ex:
		 * String[0] = "username";
		 * String[1] = "password";
		 * String[2] = "30";
		 */
		if (values.length == 3) {
			username = values[0];
			password = values[1];
			if (!values[2].isEmpty())
				projectID = Integer.parseInt(values[2]);
			else
				projectID = 0;
		}
	}
	public DownloadBatch_Input() {
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
