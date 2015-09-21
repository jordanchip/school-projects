package shared_model;

/**
 * A row in a batch.  Contains an id and row number.  Does not contain the 
 * actual values stored in the rows.
 * @author jchip
 *
 */
public class Record {

	private int ID;
	private int batchID;
	private int row;
	
	public Record() {
		ID = -1;
		batchID = -1;
		row = -1;
	}
	public Record(int batchID, int row) {
		ID = -1;
		this.batchID = batchID;
		this.row = row;
	}
	public Record(int iD, int batchID, int row) {
		ID = iD;
		this.batchID = batchID;
		this.row = row;
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
	 * @return the batchID
	 */
	public int getBatchID() {
		return batchID;
	}
	/**
	 * @param batchID the batchID to set
	 */
	public void setBatchID(int batchID) {
		this.batchID = batchID;
	}
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
}
