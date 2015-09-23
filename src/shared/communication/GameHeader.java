package shared.communication;

import java.util.List;

public class GameHeader {
	private String title;
	private int id;
	private List<PlayerHeader> players;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the players
	 */
	public List<PlayerHeader> getPlayers() {
		return players;
	}

	
}
