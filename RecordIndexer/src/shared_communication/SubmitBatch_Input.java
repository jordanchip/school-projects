package shared_communication;

import java.util.ArrayList;
import java.util.List;

import shared_model.Field;
import shared_model.Cell;

/**
 * Contains all the input for the submitBatch method. 
 * Contains the user name and password to validate, as  
 * well as the batch id and fields in the batch, and 
 * a 2d array of all the cells to be sent to 
 * the database.
 * @author jchip
 *
 */
public class SubmitBatch_Input {

	private String username;
	private String password;
	private int batchID;
	private String[][] cells;

	public SubmitBatch_Input(String[] values) {
		if (values.length == 4) {
			username = values[0];
			password = values[1];
			if (!values[2].isEmpty())
				batchID = Integer.parseInt(values[2]);
			else
				batchID = 0;
			assignCells(values[3]);
		}
	}

	public void assignCells(String values) {
		
		String[] rows = values.split(";");
		cells = new String[rows.length][rows[0].split(",").length];
		for (int i = 0; i < rows.length; i++) {
			String[] cell = rows[i].split(",");
			for (int j = 0; j < cell.length; j++) {
				cells[i][j] = cell[j];
			}
		}
	}
	
	public SubmitBatch_Input() {
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

	public int getBatchID() {
		return batchID;
	}

	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}

	public String[][] getCells() {
		return cells;
	}

	public void setCells(String[][] cells) {
		this.cells = cells;
	}
}
