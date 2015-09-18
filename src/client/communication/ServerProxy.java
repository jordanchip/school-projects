package client.communication;

import java.util.List;

import server.ai.AIType;
import server.logging.LogLevel;
import shared.communication.Command;
import shared.communication.GameHeader;
import shared.communication.IServer;
import shared.communication.Session;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.CatanModel;
import shared.model.PlayerReference;
import shared.model.ResourceList;

public class ServerProxy implements IServer {

	@Override
	public Session login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session register(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GameHeader> getGameList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session joinGame(Session user, int gameID, CatanColor color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean saveGame(Session user, int gameID, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadGame(Session user, String filename) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CatanModel getModel(Session user, int version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel resetGame(Session user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Command> getCommands(Session user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel executeCommands(Session user, List<Command> commands) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addAIPlayer(Session user, AIType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<AIType> getAITypes(Session user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel sendChat(Session user, String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel rollDice(Session user, int number) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel robPlayer(Session user, HexLocation newRobberLocation,
			PlayerReference victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buyDevCard(Session user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel yearOfPlenty(Session user, ResourceType type1,
			ResourceType type2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel roadBuilding(Session user, EdgeLocation road1,
			EdgeLocation road2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel soldier(Session user, HexLocation newRobberLocation,
			PlayerReference victim) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel monopoly(Session user, ResourceType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildRoad(Session user, EdgeLocation location,
			boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildSettlement(Session user, VertexLocation location,
			boolean free) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel buildCity(Session user, VertexLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel offerTrade(Session user, ResourceList offer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel respondToTrade(Session user, boolean accept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel maritimeTrade(Session user, ResourceType inResource,
			ResourceType outResource, int ratio) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel discardCards(Session user, ResourceList cards) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CatanModel finishTurn(Session user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean changeLogLevel(LogLevel level) {
		// TODO Auto-generated method stub
		return false;
	}

}
