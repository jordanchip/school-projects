package shared.model;

import java.util.List;

import shared.locations.HexLocation;

public class Board {
	public List<Hex> hexes;
	public List<Port> port;
	public List<EdgeObject> roads;
	public List<VertexObject> settlements;
	public List<VertexObject> cities;
	public int radius;
	public HexLocation robber;

	public Board() {
		
	}

}
