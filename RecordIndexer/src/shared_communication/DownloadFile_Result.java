package shared_communication;

/**
 * Contains the results to the downloadFile method. 
 * Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class DownloadFile_Result {

private boolean hasFailed;
	
	public boolean isHasFailed() {
		return hasFailed;
	}
	public void setHasFailed(boolean hasFailed) {
		this.hasFailed = hasFailed;
	}
	
	@Override
	public String toString() {
		return null;
	}
}
