package shared_communication;

import java.util.List;


/**
 * Contains the results to the search method.  Contains all 
 * the information about found matches.  Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class Search_Result {
	
	private String PATH_URL = "";

	private List<Integer> batchIDs;
	private List<String> imageURLs;
	private List<Integer> rowNumbers;
	private List<Integer> fieldIDs;
	private boolean hasFailed;
	
	public boolean isHasFailed() {
		return hasFailed;
	}
	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}

	public List<Integer> getBatchIDs() {
		return batchIDs;
	}


	public void setBatchIDs(List<Integer> batchIDs) {
		this.batchIDs = batchIDs;
	}


	public List<String> getImageURLs() {
		return imageURLs;
	}


	public void setImageURLs(List<String> imageURLs) {
		this.imageURLs = imageURLs;
	}


	public List<Integer> getRowNumbers() {
		return rowNumbers;
	}


	public void setRowNumbers(List<Integer> rowNumbers) {
		this.rowNumbers = rowNumbers;
	}


	public List<Integer> getFieldIDs() {
		return fieldIDs;
	}


	public void setFieldIDs(List<Integer> fieldIDs) {
		this.fieldIDs = fieldIDs;
	}


	@Override
	public String toString() {
		if (hasFailed)
			return "FAILED";
		StringBuilder returnString = new StringBuilder();
		//All the arrays should have the same size
		for (int i = 0; i < batchIDs.size(); i++) {
			returnString.append(batchIDs.get(i) + "\n");
			returnString.append(PATH_URL + "/database/Records/" + imageURLs.get(i) + "\n");
			returnString.append(rowNumbers.get(i) + "\n");
			returnString.append(fieldIDs.get(i) + "\n");
		}
		
		
		return returnString.toString();
	}
	public String getPATH_URL() {
		return PATH_URL;
	}
	public void setPATH_URL(String pATH_URL) {
		PATH_URL = pATH_URL;
	}
}
