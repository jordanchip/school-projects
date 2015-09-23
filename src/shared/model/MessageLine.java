package shared.model;

/** An immutable representation of a chat message
 * @author beefster
 *
 */
public class MessageLine {
	private String message;
	private String source;

	public MessageLine() {
		
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	
}
