package database;

import java.util.logging.*;
import java.util.*;
import java.sql.*;

import shared_model.User;

/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning users. Contains methods to add, access 
 * and modify users in the database
 * @author jchip
 *
 */
public class UserDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	public UserDAO(Database db) {
		this.db = db;
	}
	/**
	 * Creates an instance of a UserDAO to interact with the database.
	 */
	public UserDAO() {
		
	}
	
	/**
	 * Checks in the database if a user exists with the given information
	 * @param user the User that is being found
	 * @return The user with the corresponding username
	 */
	public User validateUser(User user) throws DatabaseException {
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		User returnUser = new User();

		try {
			String query = "select id, username, password, firstName, lastName,"
					+ " email, numIndexedRecords, assignedBatchID from user WHERE username = ? AND password = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String email = rs.getString(6);
				int numIndex = rs.getInt(7);
				int assignedBatch = rs.getInt(8);
				if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
					returnUser = new User(id, username, password, firstName, lastName, email, numIndex, assignedBatch);
					break;
				}
			}
			return returnUser;
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not find user");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Adds a new user to the database
	 * @param user The user to be added to the database
	 * @throws DatabaseException 
	 */
	public void addUser(User user) throws DatabaseException {
		
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into user (username, password, firstName, lastName, email, numIndexedRecords, assignedBatchID) values (?, ?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getIndexedRecords());
			stmt.setInt(7, user.getCurrentAssignedBatch());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				user.setID(id);
			}
			else {
				throw new DatabaseException("Could not add user");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add user");
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Updates a user with the new given information, like if the user 
	 * has begun to work on a batch, or if the user has finished 
	 * using a batch.
	 * @param user The user to be updated.
	 * @throws DatabaseException 
	 */
	public void updateUser(User user) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update user set username = ?, password = ?, firstName = ?, lastName = ?, email = ?, numIndexedRecords = ?, assignedBatchID = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getEmail());
			stmt.setInt(6, user.getIndexedRecords());
			stmt.setInt(7, user.getCurrentAssignedBatch());
			stmt.setInt(8, user.getID());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update record", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the users from the database
	 * @return
	 * @throws DatabaseException 
	 */
	public List<User> getAll() throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<User> users = new ArrayList<User>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select id, username, password, firstName, lastName, email, numIndexedRecords, assignedBatchID from user";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String firstName = rs.getString(4);
				String lastName = rs.getString(5);
				String email = rs.getString(6);
				int numIndex = rs.getInt(7);
				int assignedBatch = rs.getInt(8);
				
				users.add(new User(id, username, password, firstName, lastName,
									email, numIndex, assignedBatch));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update record", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return users;
	}
	
	/**
	 * Gets the user from the database with the specified ID
	 * @param id The ID of the user
	 * @return The User with the specified ID.
	 */
	public User getUser(int id) {
		return null;
	}
	
	/**
	 * Removes the user from the database with the given information
	 * @param user The user with the information to be removed.
	 * @throws DatabaseException 
	 */
	public void deleteUser(User user) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from user where username = ? and password = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getPassword());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete user");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete user", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
