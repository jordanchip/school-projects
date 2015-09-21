package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import shared_model.*;

/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning batches. Contains methods to 
 * retrieve and modify batch information in the database.
 * @author jchip
 *
 */
public class BatchDAO {
	
	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	/**
	 * Creates an instance of a BatchDAO to interact with the database.
	 */
	public BatchDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds a batch to the database.
	 * @param batch to be added.
	 * @throws DatabaseException 
	 */
	public void addBatch(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into batch (projectID, path, inUse) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getProjectID());
			stmt.setString(2, batch.getPath());
			stmt.setBoolean(3, batch.isInUse());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				batch.setID(id);
			}
			else {
				throw new DatabaseException("Could not add batch");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add batch", e);
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Sends a batch to the database to be updated, and 
	 * updates the batch to reflect its status.
	 * @param batch The batch to be submitted.
	 * @throws DatabaseException 
	 */
	public void updateBatch(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update batch set inUse = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setBoolean(1, batch.isInUse());
			stmt.setInt(2, batch.getID());
			if (stmt.executeUpdate() != 1)
				throw new DatabaseException("Could not update batch");
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update batch", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Sends a batch to the database to be updated, and 
	 * updates the batch to reflect its status.
	 * @param batch The batch to be submitted.
	 * @throws DatabaseException 
	 */
	public boolean updateBatch(Batch batch, String[][] cells) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update batch set inUse = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setBoolean(1, batch.isInUse());
			stmt.setInt(2, batch.getID());
			if (stmt.executeUpdate() != 1)
				throw new DatabaseException("Could not add batch");
			//If the batch was updated successfully, the program will attempt to
			//send the updated data cells to the database.
			else {
				batch = getBatch(batch.getID());
				
				CellDAO cellDAO = db.getCellDAO();
				FieldDAO fieldDAO = db.getFieldDAO();
				RecordDAO recordDAO = db.getRecordDAO();
				Project project = new Project(batch.getProjectID());
				
				List<Field> fields = fieldDAO.getAllFieldsForProject(project);
				Collections.sort(fields);
				
				for (int i = 0; i < cells.length; i++) {
					Record record = new Record(batch.getID(), i);
					recordDAO.addRecord(record);
					//Check if any of the given cell values don't
					//match up with the number of columns
					if (cells[i].length != fields.size()) {
						return false;
					}
					for (int j = 0; j < fields.size(); j++) {
						//Check if any cell wasn't filled when it was
						//passed in.
						if (cells[i][j] == null)
							return false;
						Cell cell = new Cell(record.getID(), fields.get(j).getID(),
											cells[i][j]);
						cellDAO.addCell(cell);
					}
				}
			}
			return true;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update batch", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets the first sample batch for the specific project.
	 * @param project The project for which a sample batch is desired.
	 * @return A sample batch
	 * @throws DatabaseException 
	 */
	public Batch getSampleBatch(Project project) throws DatabaseException {
		logger.entering("sever.database.Batch", "getSampleBatch");
		Batch batch = new Batch();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select id, projectID, path, inUse from batch where projectID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project.getID());
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt(1);
				int projectID = rs.getInt(2);
				String path = rs.getString(3);
				boolean isInUse = rs.getBoolean(4);
				batch = new Batch(id, projectID, path, isInUse);
			}
			return batch;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get sample batch", e);
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
	public List<Batch> getAll() throws DatabaseException {
		logger.entering("sever.database.Batch", "getAll");
		List<Batch> batches = new ArrayList<Batch>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from batch";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int projectID = rs.getInt(2);
				String path = rs.getString(3);
				boolean inUse = rs.getBoolean(4);
				
				batches.add(new Batch(id, projectID, path, inUse));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get batches", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return batches;
	}
	
	public Batch getBatch(int id) throws DatabaseException {
		logger.entering("sever.database.Batch", "getAll");
		Batch batch = new Batch();
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from batch where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				int Bid = rs.getInt(1);
				int projectID = rs.getInt(2);
				String path = rs.getString(3);
				boolean inUse = rs.getBoolean(4);
				
				return new Batch(Bid, projectID, path, inUse);
			}
			return batch;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get batches", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets one batch for a given project and user, and updates the batch to reflect 
	 * that it is now being used.  Also updates the user to link the user to the batch.
	 * @param project Any project for which a sample batch is desired.
	 * @param user Any user
	 * @return A batch for the user to work on
	 * @throws DatabaseException 
	 */
	public Batch getBatch(Project project, User user) throws DatabaseException {
		logger.entering("sever.database.Batch", "getBatch");
		Batch batch = new Batch();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			String query = "select id, projectID, path, inUse from batch where projectID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, project.getID());
			rs = stmt.executeQuery();
			int id = -1;
			
			while (rs.next()) {
				id = rs.getInt(1);
				int projectID = rs.getInt(2);
				String path = rs.getString(3);
				boolean isInUse = rs.getBoolean(4);
				if (!isInUse) {
					//Once we find a batch that isn't being worked on,
					//We will retrieve it from the database and set its value to true.
					batch = new Batch(id, projectID, path, true);
					break;
				}
			}
			if (batch.getID() == -1) {
				return batch;
			}
			//Now that we have the batch, its time to update the status of
			//the user that is working on the batch.
			updateBatch(batch);

			//Now we'll update the user information to show that the user is currently
			//Working on this batch.
			UserDAO userDAO = new UserDAO(db);
			user.setCurrentAssignedBatch(batch.getID());
			userDAO.updateUser(user);
			return batch;
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get batch", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);

		}
	}

	/**
	 * Removes a given batch from the database
	 * @param batch the batch to be deleted.
	 * @throws DatabaseException 
	 */
	public void deleteBatch(Batch batch) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from batch where id = ? and projectID = ? and path = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getID());
			stmt.setInt(2, batch.getProjectID());
			stmt.setString(3, batch.getPath());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete batch. FAILED");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete batch", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
