package shared_model;

/**
 * A column for values in a project.  Has a name, xCoord and id to link it to
 * its parent project.
 * Also contains the ID linking it to its corresponding batch.
 * @author jchip May 2015
 *
 */
public class Field  implements Comparable {

	
	private String name;
	private int ID;
	private int projectID;
	private int xCoord;
	private int width;
	private String helpHTMLPath;
	private String knownDataPath;
	
	
	public Field() {
		name = null;
		ID = -1;
		projectID = -1;
		xCoord = -1;
		width = -1;
		helpHTMLPath = null;
		knownDataPath = null;
	}
	
	public Field(int iD, String name, int projectID, int xCoord, int width,
			String helpHTMLPath, String knownDataPath) {
		ID = iD;
		this.name = name;
		this.projectID = projectID;
		this.xCoord = xCoord;
		this.width = width;
		this.helpHTMLPath = helpHTMLPath;
		this.knownDataPath = knownDataPath;
	}
	
	public Field(String name, int projectID, int xCoord, int width,
			String helpHTMLPath, String knownDataPath) {
		this.name = name;
		ID = -1;
		this.projectID = projectID;
		this.xCoord = xCoord;
		this.width = width;
		this.helpHTMLPath = helpHTMLPath;
		this.knownDataPath = knownDataPath;
	}

	public Field(String name) {
		this.name = name;
		ID = -1;
		projectID = -1;
		xCoord = -1;
		width = -1;
		helpHTMLPath = null;
		knownDataPath = null;
	}

	public Field(int id) {
		ID = id;
		name = null;
		projectID = -1;
		xCoord = -1;
		width = -1;
		helpHTMLPath = null;
		knownDataPath = null;
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
	 * @return the projectID
	 */
	public int getProjectID() {
		return projectID;
	}

	/**
	 * @param projectID the projectID to set
	 */
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	/**
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}

	/**
	 * @param xCoord the xCoord to set
	 */
	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the helpHTMLPath
	 */
	public String getHelpHTMLPath() {
		return helpHTMLPath;
	}

	/**
	 * @param helpHTMLPath the helpHTMLPath to set
	 */
	public void setHelpHTMLPath(String helpHTMLPath) {
		this.helpHTMLPath = helpHTMLPath;
	}

	/**
	 * @return the knownDataPath
	 */
	public String getKnownDataPath() {
		return knownDataPath;
	}

	/**
	 * @param knownDataPath the knownDataPath to set
	 */
	public void setKnownDataPath(String knownDataPath) {
		this.knownDataPath = knownDataPath;
	}

	@Override
	public int compareTo(Object o) {
		 if (!(o instanceof Field))
		      throw new ClassCastException("A Field object expected.");
		    int otherID = ((Field)o).getID();  
		    return this.ID - otherID;    
	}

	
}
