package shared_model;


/**
 * Contains the actual value of each cell in a batch, as well as index 
 * values to the corresponding record and field.
 * @author jchip
 *
 */
public class Cell {

	private int ID;
	private int recordID;
	private int fieldID;
	private String value;
	
	/**
	 * Creates a cell with default (invalid) values
	 */
	public Cell() {
		recordID = -1;
		fieldID = -1;
		value = null;
	}
	/**
	 * Creates a cell with specified values
	 * @param recordID The record ID to which the cell belongs
	 * @param fieldID The field ID to which the cell belongs
	 * @param value The actual value of the cell
	 */
	public Cell(int recordID, int fieldID, String value) {
		ID = -1;
		this.recordID = recordID;
		this.fieldID = fieldID;
		this.value = value;
	}
	public Cell(int ID, int recordID, int fieldID, String value) {
		this.ID = ID;
		this.recordID = recordID;
		this.fieldID = fieldID;
		this.value = value;
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
	 * @return the recordID
	 */
	public int getRecordID() {
		return recordID;
	}
	/**
	 * @param recordID the recordID to set
	 */
	public void setRecordID(int recordID) {
		this.recordID = recordID;
	}
	/**
	 * @return the fieldID
	 */
	public int getFieldID() {
		return fieldID;
	}
	/**
	 * @param fieldID the fieldID to set
	 */
	public void setFieldID(int fieldID) {
		this.fieldID = fieldID;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
}
