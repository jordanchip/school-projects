package shared.model;

import java.util.Map;

import shared.definitions.*;

public class ResourceTradeList {
	
	Map<ResourceType, Integer> offerred;
	Map<ResourceType, Integer> wanted;
	
	/** Makes this trade between two ResourceLists
	 * @param offerer the ResourceList to trade from
	 * @param receiver the ResourceList to trade to
	 * @pre The offerrer has at least what is offerred and the receiver has at least what
	 * is wanted.
	 * @post The offerred resources will be taken from the offerrer and given to the receiver
	 * and the wanted resources will be taken from the receiver and given to the offerrer.
	 */
	public void makeExchange(ResourceList offerer, ResourceList receiver) {
		
	}

}
