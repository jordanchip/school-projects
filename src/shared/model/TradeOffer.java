package shared.model;

/**
 * Manages the current trade offer between two players
 */
class TradeOffer {
	private PlayerReference sender;
	private PlayerReference receiver;
	private ResourceList offer;

	public TradeOffer() {
		
	}

	/**
	 * @return the sender
	 */
	public PlayerReference getSender() {
		return sender;
	}

	/**
	 * @return the receiver
	 */
	public PlayerReference getReceiver() {
		return receiver;
	}

	/**
	 * @return the offer
	 */
	public ResourceList getOffer() {
		return offer;
	}
	
	/** Accepts the offer
	 * @pre None
	 * @post The offer is accepted and will be carried out.
	 */
	public void accept() {
		
	}
	
	/** Declines the offer
	 * @pre None
	 * @post The offer is declined and gameplay continues as before.
	 */
	public void decline() {
		
	}

}
