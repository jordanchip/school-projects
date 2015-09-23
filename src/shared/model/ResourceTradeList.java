package shared.model;

import java.util.HashMap;
import java.util.Map;

import shared.definitions.*;
import shared.exceptions.TradeException;

/** An immutable representation of an exchange of resources.
 * @author beefster
 *
 */
public class ResourceTradeList {
	
	Map<ResourceType, Integer> offered;
	Map<ResourceType, Integer> wanted;
	
	/**
	 * @return a copy of the offered resources, as a Map
	 */
	public Map<ResourceType, Integer> getOffered() {
		return new HashMap<>(offered);
	}

	/**
	 * @return a copy of the wanted resources, as a Map
	 */
	public Map<ResourceType, Integer> getWanted() {
		return new HashMap<>(wanted);
	}

	/** Makes this trade between two ResourceLists
	 * @param offerer the ResourceList to trade from
	 * @param receiver the ResourceList to trade to
	 * @pre <ul>
	 * <li>The offerer has at least what is offered</li>
	 * <li>the receiver has at least what is wanted.</li>
	 * </ul>
	 * @post The offered resources will be taken from the offerer and given to the receiver
	 * and the wanted resources will be taken from the receiver and given to the offerer.
	 * @throws TradeException if either of the preconditions are not met
	 */
	public void makeExchange(ResourceList offerer, ResourceList receiver) throws TradeException {
		
	}

}
