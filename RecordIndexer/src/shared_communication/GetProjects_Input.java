package shared_communication;

/**
 * Contains the input to the getProject method. 
 * Contains a username and password to validate and search for all projects.
 * @author jchip
 *
 */
public class GetProjects_Input {

	private String username;
	private String password;
	
	public GetProjects_Input(String[] args) {
		if (args.length == 2) {
			username = args[0];
			password = args[1];
		}
	}
	public GetProjects_Input() {
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
}
