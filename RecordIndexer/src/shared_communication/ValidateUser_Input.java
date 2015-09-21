package shared_communication;

/**
 * Contains all the input information for the validateUser method. 
 * Contains the username and password of a user to validate.
 * @author jchip
 *
 */
public class ValidateUser_Input {

	private String username;
	private String password;
	
	public ValidateUser_Input(String[] args) {
		if (args.length == 2) {
			username = args[0];
			password = args[1];
		}
	}
	
	public ValidateUser_Input() {
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
