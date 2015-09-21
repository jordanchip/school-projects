package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared_model.Batch;
import shared_model.Project;
import shared_model.User;


/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning projects. Contains methods to add
 * and access projects in the database.
 * @author jchip
 *
 */
public class ProjectDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	/**
	 * Creates an instance of a ProjectDAO to interact with the database.
	 */
	public ProjectDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds a project to the database.
	 * @param project The project to be added.
	 * @throws DatabaseException 
	 */
	public void addProject(Project project) throws DatabaseException {

		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into project (name, recordsPerImage, firstYCoord, recordsHeight) values (?, ?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getName());
			String rpi = Integer.toString(project.getRecordsPerImage());
			String fyc = Integer.toString(project.getFirstYCoord());
			String rh = Integer.toString(project.getRecordsHeight());
			stmt.setString(2, rpi);
			stmt.setString(3, fyc);
			stmt.setString(4, rh);
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				project.setID(id);
			}
			else {
				throw new DatabaseException("Could not add project");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add project",e);
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the projects available in the database. Searches by ID.
	 * @return A list of all the projects.
	 * @throws DatabaseException 
	 */
	public Project getProject(Project project) throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		Project returnProject = new Project();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select id, name, recordsPerImage, firstYCoord, recordsHeight from project where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project.getID());
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int recPerImage = rs.getInt(3);
				int firstYCoord = rs.getInt(4);
				int recHeight = rs.getInt(5);
				
				returnProject.setID(id);
				returnProject.setName(name);
				returnProject.setRecordsPerImage(recPerImage);
				returnProject.setFirstYCoord(firstYCoord);
				returnProject.setRecordsHeight(recHeight);
				return returnProject;
			}
			return returnProject;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add project",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the projects available in the database. Searches by ID.
	 * @return A list of all the projects.
	 * @throws DatabaseException 
	 */
	public Project getProjectFromBatch(Batch batch) throws DatabaseException {
		logger.entering("sever.database.User", "getProjectFromBatch");
		Project returnProject = new Project();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select id, name, recordsPerImage, firstYCoord, recordsHeight from project where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProjectID());
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int recPerImage = rs.getInt(3);
				int firstYCoord = rs.getInt(4);
				int recHeight = rs.getInt(5);
				
				returnProject.setID(id);
				returnProject.setName(name);
				returnProject.setRecordsPerImage(recPerImage);
				returnProject.setFirstYCoord(firstYCoord);
				returnProject.setRecordsHeight(recHeight);
				return returnProject;
			}
			return returnProject;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get project",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the projects available in the database.
	 * @return A list of all the projects.
	 * @throws DatabaseException 
	 */
	public List<Project> getAll() throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Project> projects = new ArrayList<Project>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select id, name, recordsPerImage, firstYCoord, recordsHeight from project";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				String name = rs.getString(2);
				int recPerImage = rs.getInt(3);
				int firstYCoord = rs.getInt(4);
				int recHeight = rs.getInt(5);
				
				projects.add(new Project(id, name, recPerImage, firstYCoord, recHeight));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add project",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return projects;
	}
	
	/**
	 * Updates a project in the database with the specified information
	 * @param project The project with the information to be updated.
	 * @throws DatabaseException 
	 */
	public void updateProject(Project project) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update project set name = ?, recordsPerImage = ?, firstYCoord = ?, recordsHeight = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, project.getName());
			stmt.setInt(2, project.getRecordsPerImage());
			stmt.setInt(3, project.getFirstYCoord());
			stmt.setInt(4, project.getRecordsHeight());
			stmt.setInt(5, project.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update project");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update project",e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Removes a project from the database with the given information
	 * @param project The project with the information to be removed.
	 * @throws DatabaseException 
	 */
	public void deleteProject(Project project) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from project where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project.getID());
			
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
