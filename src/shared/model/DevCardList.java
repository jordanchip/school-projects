package shared.model;

import java.util.Map;

import shared.definitions.DevCardType;

/**
 * Manages the development cards that a player or the bank has.
 */
public class DevCardList {
	private Map<DevCardType, Integer> cards;

	public DevCardList() {
		
	}
	
	public int count() {
		int total = 0;
		for (int count : cards.values()) {
			total += count;
		}
		return total;
	}
	
	public int count(DevCardType type) {
		return cards.get(type);
	}
	
	/** Transfers a card from this DevCardList to another
	 * @param destination the DevCardList to transfer to
	 * @param type the type of card you want to transfer
	 * @pre count(type) >= 1
	 * @post the count of the specified type will be decreased on this DevCardList
	 * and increased on the destination DevCardList
	 */
	public void transferCardTo(DevCardList destination, DevCardType type) {
		
	}
	

	/** Transfers a random card from this DevCardList to another, with a uniform
	 * distribution based on counts
	 * @param destination the DevCardList to transfer to
	 * @pre count() >= 1
	 * @post the count of the randomly determined type will be decreased on this 
	 * DevCardList and increased on the destination DevCardList
	 */
	public void transferRandomCardTo(DevCardList destination) {
		
	}
	
	/** Uses a card of the given type
	 * @param type The type of the card to use
	 * @pre count(type) >= 1
	 * @post the count of the card's type will decrease by 1 and the appropriate action will be taken
	 */
	public void useCard(DevCardType type) {
		
	}

}