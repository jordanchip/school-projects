package shared.communication;

import java.util.List;

import server.ai.AIType;
import server.logging.LogLevel;
import shared.definitions.*;
import shared.locations.*;
import shared.model.*;

public interface IServer {

	public Session login(String username, String password);
	public Session register(String username, String password);
	
	public List<GameHeader> getGameList();
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts);
	public Session joinGame(Session user, int gameID, CatanColor color);
	public boolean saveGame(Session user, int gameID, String filename);
	public boolean loadGame(Session user, String filename);
	
	public CatanModel getModel(Session user, int version);
	public CatanModel resetGame(Session user);
	public List<Command> getCommands(Session user);
	public CatanModel executeCommands(Session user, List<Command> commands);
	public boolean addAIPlayer(Session user, AIType type);
	public List<AIType> getAITypes(Session user);

	public CatanModel sendChat(Session user, String message);
	public CatanModel rollDice(Session user, int number);
	public CatanModel robPlayer(Session user,
			HexLocation newRobberLocation, PlayerReference victim);
	public CatanModel buyDevCard(Session user);
	public CatanModel yearOfPlenty(Session user, ResourceType type1, ResourceType type2);
	public CatanModel roadBuilding(Session user, EdgeLocation road1, EdgeLocation road2);
	public CatanModel soldier(Session user, 
			HexLocation newRobberLocation, PlayerReference victim);
	public CatanModel monopoly(Session user, ResourceType type);
	public CatanModel buildRoad(Session user, EdgeLocation location, boolean free);
	public CatanModel buildSettlement(Session user, VertexLocation location, boolean free);
	public CatanModel buildCity(Session user, VertexLocation location);
	public CatanModel offerTrade(Session user, ResourceList offer);
	public CatanModel respondToTrade(Session user, boolean accept);
	public CatanModel maritimeTrade(Session user,
			ResourceType inResource, ResourceType outResource, int ratio);
	public CatanModel discardCards(Session user, ResourceList cards);
	public CatanModel finishTurn(Session user);
	
	public boolean changeLogLevel(LogLevel level);
}
