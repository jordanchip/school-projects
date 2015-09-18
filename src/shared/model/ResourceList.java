package shared.model;

import java.util.Map;

import shared.definitions.ResourceType;

import java.util.*;

/** A ResourceList where all counts must be non-negative
 * @author beefster
 *
 */
public class ResourceList {
	private Map<ResourceType, Integer> resources;
	
	public ResourceList() {
		
	}
	
	/** Counts the total number of cards of the given type in this ResourceList
	 * @param type
	 * @return The number of cards of the given type
	 * @pre none
	 * @post none
	 */
	public int count(ResourceType type) {
		return resources.get(type);
	}

	/** Transfers cards from one ResourceList to another
	 * @param destination the ResourceList to transfer to
	 * @param type the Type of resource to transfer
	 * @param amount the number of cards to transfer
	 * @pre This ResourceList has at least amount cards in it
	 * @post This list's number of cards will decrease by amount and the destination
	 * list will increase by amount
	 */
	public void transferTo(ResourceList destination, ResourceType type, int amount) {
		
	}
	
	/** Counts the total number of cards in this ResourceList
	 * @return The number of cards
	 * @pre none
	 * @post none
	 */
	public int count() {
		int total = 0;
		for (int count : resources.values()) {
			total += count;
		}
		return total;
	}

}
