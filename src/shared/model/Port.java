package shared.model;

import shared.definitions.ResourceType;
import shared.locations.*;

public class Port {
	private ResourceType resource;
	private HexLocation location;
	private EdgeDirection direction;
	private int ratio;

	public Port() {
		
	}

	/**
	 * @return the resource
	 */
	public ResourceType getResource() {
		return resource;
	}

	/**
	 * @return the location
	 */
	public HexLocation getLocation() {
		return location;
	}

	/**
	 * @return the direction
	 */
	public EdgeDirection getDirection() {
		return direction;
	}

	/**
	 * @return the ratio
	 */
	public int getRatio() {
		return ratio;
	}
	
	

}
