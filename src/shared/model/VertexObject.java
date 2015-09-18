package shared.model;

import shared.locations.EdgeLocation;

class VertexObject {
	private PlayerReference owner;
	private EdgeLocation location;

	public VertexObject() {
		
	}

	/**
	 * @return the owner
	 */
	public PlayerReference getOwner() {
		return owner;
	}

	/**
	 * @return the location
	 */
	public EdgeLocation getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(EdgeLocation location) {
		this.location = location;
	}

}