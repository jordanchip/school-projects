package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared_model.Field;
import shared_model.Project;

/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning fields. Contains methods used to add 
 * and access fields in the database.
 * @author jchip
 *
 */
public class FieldDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	/**
	 * Creates an instance of a FieldDAO to interact with the database.
	 */
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds a given field to the database.
	 * @param field The field to be added.
	 * @throws DatabaseException 
	 */
	public void addField(Field field) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into field (projectID, name, xCoord, width, helpHTMLPath, knownDataPath) values (?, ?, ?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field.getProjectID());
			stmt.setString(2, field.getName());
			stmt.setInt(3, field.getxCoord());
			stmt.setInt(4, field.getWidth());
			stmt.setString(5, field.getHelpHTMLPath());
			stmt.setString(6, field.getKnownDataPath());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				field.setID(id);
			}
			else {
				throw new DatabaseException("Could not add field");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add field",e);
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the fields in the database
	 * @return All the fields in the database
	 * @throws DatabaseException 
	 */
	public List<Field> getAll() throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Field> fields = new ArrayList<Field>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from field";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int projectID = rs.getInt(2);
				String name = rs.getString(3);
				int xCoord = rs.getInt(4);
				int width = rs.getInt(5);
				String HTMLpath = rs.getString(6);
				String knownDataPath = rs.getString(7);
				
				fields.add(new Field(id, name, projectID, xCoord, width, HTMLpath, knownDataPath));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get fields",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return fields;
	}
	
	/**
	 * Gets all the fields in the database for a given project.
	 * @param project The project which we want the fields for.
	 * @return a list of all the fields in the database for a project.
	 * @throws DatabaseException 
	 */
	public List<Field> getAllFieldsForProject(Project project) throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Field> fields = new ArrayList<Field>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from field where projectID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project.getID());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int projectID = rs.getInt(2);
				String name = rs.getString(3);
				int xCoord = rs.getInt(4);
				int width = rs.getInt(5);
				String HTMLpath = rs.getString(6);
				String knownDataPath = rs.getString(7);
				
				fields.add(new Field(id, name, projectID, xCoord, width, HTMLpath, knownDataPath));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get fields",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return fields;
	}
	
	/**
	 * Updatesa field in the database with the given information
	 * @param field The field with the new information to be updated.
	 * @throws DatabaseException 
	 */
	public void updateField(Field field) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update field set projectID = ?, name = ?, xCoord = ?, width = ?, helpHTMLPath = ?, knownDataPath = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field.getProjectID());
			stmt.setString(2, field.getName());
			stmt.setInt(3, field.getxCoord());
			stmt.setInt(4, field.getWidth());
			stmt.setString(5, field.getHelpHTMLPath());
			stmt.setString(6, field.getKnownDataPath());
			stmt.setInt(7, field.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update field");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update field",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Removes a field from the database with the given information
	 * @param field The field with the information to be removed.
	 * @throws DatabaseException 
	 */
	public void deleteField(Field field) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from field where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, field.getID());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete project", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
