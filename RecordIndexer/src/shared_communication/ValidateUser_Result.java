package shared_communication;

import shared_model.User;

/**
 * Contains the results to the validateUser method.  Contains the 
 * User object with validated information. If the user was not 
 * validated, contains a User with default values. Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class ValidateUser_Result {

	private User user;
	
	@Override
	public String toString() {
		//If the user's id is -1, it means no user was found
		if (user.getID() == -1) {
			return "FALSE\n";
		}
		else {
			return "TRUE\n" + user.getFirstName() + "\n" + user.getLastName() + "\n" + user.getIndexedRecords() + "\n";
		}
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
