package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared_model.Cell;

/**
 * A class used to implement the communication between the server and the actual database, 
 * specifically concerning cells. Contains addCell method to add 
 * a cell into the database.
 * @author jchip
 *
 */
public class CellDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	/**
	 * Creates an instance of a CellDAO to interact with the database.
	 */
	public CellDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Adds the given cell to the database
	 * @param cell The cell to be added
	 * @throws DatabaseException 
	 */
	public void addCell(Cell cell) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet keyRS = null;
		try {
			String query = "insert into cell (recordID, fieldID, value) values (?, ?, ?)";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getRecordID());
			stmt.setInt(2, cell.getFieldID());
			stmt.setString(3, cell.getValue().toLowerCase());
			if (stmt.executeUpdate() == 1) {
				Statement keyStmt = db.getConnection().createStatement();
				keyRS = keyStmt.executeQuery("select last_insert_rowid()");
				keyRS.next();
				int id = keyRS.getInt(1);
				cell.setID(id);
			}
			else {
				throw new DatabaseException("Could not add cell");
			}
			
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not add cell", e);
		}
		finally{
			Database.safeClose(keyRS);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Gets all the cells in the database.
	 * @return All the cells in the database.
	 * @throws DatabaseException 
	 */
	public List<Cell> getAll() throws DatabaseException {
		logger.entering("sever.database.User", "getAll");
		List<Cell> cells = new ArrayList<Cell>();
		
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "select * from cell";
			stmt = db.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt(1);
				int recordID = rs.getInt(2);
				int fieldID = rs.getInt(3);
				String width = rs.getString(4);
				
				cells.add(new Cell(id, recordID, fieldID, width));
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not get cells", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
		return cells;
	}
	
	/**
	 * Updates a cell with the given ID in the parameters;
	 * @param cell the cell containing the information to be updated.
	 * @throws DatabaseException 
	 */
	public void updateCell(Cell cell) throws DatabaseException {
		PreparedStatement stmt = null;
		ResultSet rs = null;		
		try {
			String query = "update cell set value = ? where recordID = ? and fieldID = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setString(1, cell.getValue());
			stmt.setInt(2, cell.getRecordID());
			stmt.setInt(3, cell.getFieldID());
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not update cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not update cell", e);
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
	}
	
	/**
	 * Removes a cell from the database
	 * @param cell The cell with the information to be deleted.
	 * @throws DatabaseException 
	 */
	public void deleteCell(Cell cell) throws DatabaseException {
		PreparedStatement stmt = null;	
		try {
			String query = "delete from cell where id = ?";
			stmt = db.getConnection().prepareStatement(query);
			stmt.setInt(1, cell.getID());
			
			if (stmt.executeUpdate() != 1) {
				throw new DatabaseException("Could not delete cell");
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Could not delete cell", e);
		}
		finally {
			Database.safeClose(stmt);
		}
	}
	
}
