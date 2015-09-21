package shared_communication;

import shared_model.Batch;

/**
 * Contains the results to the getSampleImage method.  Contains the 
 * batch information to get the actual sample image.  Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class GetSampleImage_Result {

	private String PATH_URL = "";
	
	private Batch batch = new Batch();
	private boolean hasFailed = false;
	
	public boolean isHasFailed() {
		return hasFailed;
	}
	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}
	
	@Override
	public String toString() {
		if (hasFailed)
			return "FAILED";
		return PATH_URL + "/database/Records/" + batch.getPath() + "\n";
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	public String getPATH_URL() {
		return PATH_URL;
	}
	public void setPATH_URL(String pATH_URL) {
		PATH_URL = pATH_URL;
	}
}
