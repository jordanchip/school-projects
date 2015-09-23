package shared.model;

/** Represents an immutable reference to a player in a game
 * @author Justin
 *
 */
public class PlayerReference {
	private CatanModel game;
	private int playerIndex;
	
	public PlayerReference() {
		
	}
	
	/** Gets the player that this object references.
	 * @return the player 'pointed' to by this PlayerReference
	 */
	public Player getPlayer() {
		return game.getPlayers().get(playerIndex);
	}
}
