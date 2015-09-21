package shared_model;



/**
 * A user class containing all the information of a user:
 * username, password, first and last name,
 * number of previously indexed records and the unique user ID.  
 * Contains getters and setters for each field
 * @author jchip May 2015
 *
 */
public class User {

	private int ID;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private int indexedRecords;
	private int currentAssignedBatch;
	
	/**
	 * Creates a new User with everything set to null,
	 * and user ID set to -1.
	 */
	public User() {
		ID = -1;
		username = null;
		password = null;
		firstName = null;
		lastName = null;
		email = null;
		currentAssignedBatch = 0;
		indexedRecords = 0;
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		firstName = null;
		lastName = null;
		email = null;
		currentAssignedBatch = 0;
		indexedRecords = 0;
		ID = -1;
	}
	
	public User(int ID, String username, String password, String firstName, String lastName,
			String email, int indexedRecords, int currentAssignedBatch) {
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.indexedRecords = indexedRecords;
		this.currentAssignedBatch = currentAssignedBatch;
	}
	
	public User(String username, String password, String firstName, String lastName,
				String email, int indexedRecords, int currentAssignedBatch) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.indexedRecords = indexedRecords;
		this.currentAssignedBatch = currentAssignedBatch;
		ID = -1;
	}

	public User(String username, String password, String firstName,
			String lastName, String email, int indexedRecords) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.indexedRecords = indexedRecords;
		currentAssignedBatch = 0;
		ID = -1;
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the indexedRecords
	 */
	public int getIndexedRecords() {
		return indexedRecords;
	}

	/**
	 * @param indexedRecords the indexedRecords to set
	 */
	public void setIndexedRecords(int indexedRecords) {
		this.indexedRecords = indexedRecords;
	}

	/**
	 * @return the currentAssignedBatch
	 */
	public int getCurrentAssignedBatch() {
		return currentAssignedBatch;
	}

	/**
	 * @param currentAssignedBatch the currentAssignedBatch to set
	 */
	public void setCurrentAssignedBatch(int currentAssignedBatch) {
		this.currentAssignedBatch = currentAssignedBatch;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + indexedRecords;
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ID;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (indexedRecords != other.indexedRecords)
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (ID != other.ID)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [ID=" + ID + ", username=" + username + ", password="
				+ password + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", indexedRecords="
				+ indexedRecords + ", currentAssignedBatch="
				+ currentAssignedBatch + "]";
	}
	
	
	
}
