package database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import shared_communication.Search_Result;
import shared_model.*;

public class SearchDAO {

	private static Logger logger;
	
	static {
		logger = Logger.getLogger("RecordIndexer");
	}

	private Database db;
	
	public SearchDAO(Database db) {
		this.db = db;
	}
	
	/**
	 * Searches the database for any of the given searchValues in any
	 * of the given fields.
	 * @param fields All fields for which the user wants to search.
	 * @param searchValues All Strings which will be searched for.
	 * @return A Search_Result object which contains all the found information.
	 * If no values were found, it will have empty objects
	 * @throws DatabaseException If any errors were encountered, will throw this.
	 * Also, if the given field input OR search Values had size = 0, will throw an exception.
	 */
	public Search_Result search(List<Integer> fields, List<String> searchValues) throws DatabaseException {
		
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		Search_Result results = new Search_Result();

		try {
			String query = "select batch.id, batch.path, record.row, field.id from batch,"
					+ " record, field, cell where record.id = cell.recordID and"
					+ " field.id = cell.fieldID and record.batchID = batch.id and ";
			
			//Extends the query to perform a 4-table join.
			//Use the resulting join to search all the given
			//Fields for any of the given search values.
			StringBuilder fullQuery = new StringBuilder(query);
			addFieldsToBuilder(fullQuery, fields);
			addSearchValuesToBuilder(fullQuery, searchValues);
			query = fullQuery.toString();
			
			stmt = db.getConnection().prepareStatement(query);
			editPreparedStatement(stmt, fields, searchValues);
			rs = stmt.executeQuery();
			
			List<Integer> batchIDs = new ArrayList<Integer>();
			List<String> imageURLs = new ArrayList<String>();
			List<Integer> rowNumbers = new ArrayList<Integer>();
			List<Integer> fieldIDs = new ArrayList<Integer>();
			
			while (rs.next()) {
				batchIDs.add(rs.getInt(1));
				imageURLs.add(rs.getString(2));
				rowNumbers.add(rs.getInt(3));
				fieldIDs.add(rs.getInt(4));
			}
			
			results.setBatchIDs(batchIDs);
			results.setImageURLs(imageURLs);
			results.setRowNumbers(rowNumbers);
			results.setFieldIDs(fieldIDs);
			return results;
		}
		catch(SQLException e) {
			throw new DatabaseException("Could not find searched queries");
		}
		finally {
			Database.safeClose(rs);
			Database.safeClose(stmt);
		}
		
	}
	
	private void addFieldsToBuilder(StringBuilder current, List<Integer> fields) {
		current.append("lower(");
		boolean first = true;
		for (@SuppressWarnings("unused") Integer i : fields) {
			if (!first) 
				current.append(" OR ");
			else 
				first = false;
			current.append("field.id = ?");
		}
		current.append(")");
	}
	
	private void addSearchValuesToBuilder(StringBuilder current, List<String> searchValues) {
		current.append(" AND lower(");
		boolean first = true;
		for (@SuppressWarnings("unused") String s : searchValues) {
			if (!first) 
				current.append(" OR ");
			else 
				first = false;
			current.append("cell.value = ?");
		}
		current.append(")");
	}
	
	private void editPreparedStatement(PreparedStatement stmt, 
									List<Integer> fields, List<String> searchValues) throws DatabaseException {
		try {
			int placeHolder = 0;
			for (int i = 0; i < fields.size(); i++) {
				stmt.setInt(i+1, fields.get(i));
				placeHolder++;
			}
			for (int i = 0; i < searchValues.size(); i++) {
				stmt.setString(placeHolder+1, searchValues.get(i));
				placeHolder++;
			}
		} catch (SQLException e) {
			throw new DatabaseException("Could not find searched queries");
		}
	}
	
}