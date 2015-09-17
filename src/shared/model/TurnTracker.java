package shared.model;

/*Keeps track of whose turn it is, as well what part of their turn it is.*/
class TurnTracker {
	private PlayerReference currentPlayer;
	private TurnStatus status;

	public TurnTracker() {
		
	}

	/**
	 * @return the status
	 */
	public TurnStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TurnStatus status) {
		this.status = status;
	}

	/**
	 * @return the currentPlayer
	 */
	public PlayerReference getCurrentPlayer() {
		return currentPlayer;
	}
	
	/** Passes the turn to the next player
	 * @pre The current player has finished all mandatory actions
	 * @post Control is passed onto the next player
	 */
	public void passTurn() {
		
	}

}

