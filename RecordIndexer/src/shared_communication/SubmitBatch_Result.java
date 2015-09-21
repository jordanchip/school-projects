package shared_communication;

/**
 * Contains the results to the submitBatch method. 
 * Also contains a toString 
 * method to view the result in String format.
 * @author jchip
 *
 */
public class SubmitBatch_Result {

	private boolean wasSuccessful;
	private boolean hasFailed;
	
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
		return "TRUE\n";
	}
	public boolean isWasSuccessful() {
		return wasSuccessful;
	}
	public void setWasSuccessful(boolean wasSuccessful) {
		this.wasSuccessful = wasSuccessful;
	}
}
