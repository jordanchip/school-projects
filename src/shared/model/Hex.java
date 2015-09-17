package shared.model;

import shared.locations.HexLocation;

class Hex {
	private HexLocation location;
	private ResourceType resource;
	private int number;

	public Hex() {
		
	}

	/**
	 * @return the location
	 */
	public HexLocation getLocation() {
		return location;
	}

	/**
	 * @return the resource
	 */
	public ResourceType getResource() {
		return resource;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

}
