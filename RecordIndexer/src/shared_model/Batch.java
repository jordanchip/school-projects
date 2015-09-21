package shared_model;

/**
 * An image in a indexing project.  Does not actually
 * contain the image, just the path to the actual image itself.
 * Also contains the projectID to which the batch is linked.
 * @author jchip May 2015
 *
 */
public class Batch {

	private int ID;
	private int projectID;
	private String path;
	private boolean inUse;
	
	/**
	 * Creates a batch with default (invalid) values
	 */
	public Batch() {
		ID = -1;
		projectID = -1;
		path = null;
		inUse = false;
	}
	public Batch(int ID) {
		this.ID = ID;
		projectID = -1;
		path = null;
		inUse = false;
	}
	public Batch(int projectID, String path) {
		ID = -1;
		inUse = false;
		this.projectID = projectID;
		this.path = path;
	}
	/**
	 * Creates a Batch with specified values
	 * @param iD unique ID of the batch.
	 * @param projectID The batch's project's ID.
	 * @param path The actual path to the image on disk.
	 * @param inUse Whether or not this batch is currently being used.
	 */
	public Batch(int iD, int projectID, String path, boolean inUse) {
		ID = iD;
		this.projectID = projectID;
		this.path = path;
		this.inUse = inUse;
	}
	
	public Batch(int projectID, String path, boolean inUse) {
		ID = -1;
		this.projectID = projectID;
		this.path = path;
		this.inUse = inUse;
	}

	/**
	 * Gets ID
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}
	/**
	 * Sets the ID
	 * @param iD the new ID
	 */
	public void setID(int iD) {
		ID = iD;
	}
	/**
	 * Gets the batch's project's ID
	 * @return the projectID of this batch.
	 */
	public int getProjectID() {
		return projectID;
	}
	/**
	 * Sets the batch's project's ID
	 * @param projectID The new project ID of this batch.
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	/**
	 * Gets the actual path on disk of where the image is located.
	 * @return The path where the batch image is located on disk.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * Sets the path to the batch image.
	 * @param path The path to the image on disk.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * Checks if this batch is currently being used by a user
	 * @return true if the batch is in use.  Otherwise, false.
	 */
	public boolean isInUse() {
		return inUse;
	}
	/**
	 * Updates whether the batch is in Use.
	 * @param inUse true if the batch is in use.
	 */
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
}
