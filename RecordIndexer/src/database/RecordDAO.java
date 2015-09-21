package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared_model.Batch;
import shared_model.Record;
import shared_model.Project;

/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning records. Contains methods to add 
 * and access records in the database.
 * @author jchip
 *
 */
public class RecordDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	/**
	 * Creates an instance of a RecordDAO to interact with the database.
	 */
	public RecordDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds a given record to the database.
	 * @param record the record to be added.
	 * @throws DatabaseException 
	 */
	public void addRecord(Record record) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;		
		try {
			String query = "insert into record (batchID, row) values (?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getBatchID());
			stmt.setInt(2, record.getRow());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				record.setID(id);
			}
			else {
				throw new DatabaseException("Could not add record");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add record", e);
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	/**
	 * Gets all the records for all projects in the database
	 * @return A list of all the records for all the projects
	 * @throws DatabaseException 
	 */
	public List<Record> getAll() throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Record> records = new ArrayList<Record>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from record";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int batchID = rs.getInt(2);
				int row = rs.getInt(3);
				
				records.add(new Record(id, batchID, row));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update record", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return records;
	}
	/**
	 * Gets all the records for all projects in the database
	 * @return A list of all the records for all the projects
	 * @throws DatabaseException 
	 */
	public List<Record> getAllForBatch(Batch batch) throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Record> records = new ArrayList<Record>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from record where batchID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, batch.getID());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int batchID = rs.getInt(2);
				int row = rs.getInt(3);
				
				records.add(new Record(id, batchID, row));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update record", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return records;
	}
	
	/**
	 * Gets all the records for a given project
	 * @param project The project containing all the desired records
	 * @return A list of all records belonging to that project
	 */
	public List<Record> getRecords(Project project) {
		return null;
	}
	
	/**
	 * Updates a record in the database with the given information
	 * @param record The record containing the new information
	 * @throws DatabaseException 
	 */
	public void updateRecord(Record record) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update record set batchID = ?, row = ? where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getBatchID());
			stmt.setInt(2, record.getRow());
			stmt.setInt(3, record.getID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update record");
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
	 * Removes a record in the databse with the given information
	 * @param record The record with the information to be removed.
	 * @throws DatabaseException 
	 */
	public void deleteRecord(Record record) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from record where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, record.getID());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete record");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete record", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
}
