package shared_model;


/**
 * A project containing all the batches of a certain time.  
 * Does not actually contain the batches.  The batches themselves 
 * contain references to the Project object to link them together.
 * @author jchip
 *
 */
public class Project {


	private int ID;
	private String name;
	private int recordsPerImage;
	private int firstYCoord;
	private int recordsHeight;
	
	public Project() {
		name = null;
		recordsPerImage = -1;
		firstYCoord = -1;
		recordsHeight = -1;
		ID = -1;
	}
	public Project(int ID) {
		this.ID = ID;
		name = null;
		recordsPerImage = -1;
		firstYCoord = -1;
		recordsHeight = -1;
	}
	public Project(int ID, String name, int recordsPerImage, int firstYCoord,
			int recordsHeight) {
		this.ID = ID;
		this.name = name;
		this.recordsPerImage = recordsPerImage;
		this.firstYCoord = firstYCoord;
		this.recordsHeight = recordsHeight;
	}
	public Project(String name, int recordsPerImage, int firstYCoord,
			int recordsHeight) {
		ID = -1;
		this.name = name;
		this.recordsPerImage = recordsPerImage;
		this.firstYCoord = firstYCoord;
		this.recordsHeight = recordsHeight;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the recordsPerImage
	 */
	public int getRecordsPerImage() {
		return recordsPerImage;
	}
	/**
	 * @param recordsPerImage the recordsPerImage to set
	 */
	public void setRecordsPerImage(int recordsPerImage) {
		this.recordsPerImage = recordsPerImage;
	}
	/**
	 * @return the firstYCoord
	 */
	public int getFirstYCoord() {
		return firstYCoord;
	}
	/**
	 * @param firstYCoord the firstYCoord to set
	 */
	public void setFirstYCoord(int firstYCoord) {
		this.firstYCoord = firstYCoord;
	}
	/**
	 * @return the recordsHeight
	 */
	public int getRecordsHeight() {
		return recordsHeight;
	}
	/**
	 * @param recordsHeight the recordsHeight to set
	 */
	public void setRecordsHeight(int recordsHeight) {
		this.recordsHeight = recordsHeight;
	}
	
	
	
}
