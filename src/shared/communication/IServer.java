package shared.communication;

import java.util.List;

import server.ai.AIType;
import server.logging.LogLevel;
import shared.definitions.*;
import shared.exceptions.GameInitializationException;
import shared.exceptions.GamePersistenceException;
import shared.exceptions.InvalidActionException;
import shared.exceptions.JoinGameException;
import shared.exceptions.NotYourTurnException;
import shared.exceptions.ServerException;
import shared.exceptions.TradeException;
import shared.exceptions.UserException;
import shared.locations.*;
import shared.model.*;

public interface IServer {

	public Session login(String username, String password)
			throws UserException, ServerException;
	public Session register(String username, String password)
			throws UserException, ServerException;
	
	public List<GameHeader> getGameList() throws ServerException;
	public GameHeader createGame(Session user, String name,
			boolean randomTiles, boolean randomNumbers, boolean randomPorts)
			throws GameInitializationException, ServerException;
	public Session joinGame(Session user, int gameID, CatanColor color)
			throws JoinGameException, ServerException;
	public void saveGame(int gameID, String filename)
			throws GamePersistenceException, ServerException;
	public void loadGame(String filename)
			throws GamePersistenceException, ServerException;
	
	public CatanModel getModel(Session user, int version) throws ServerException;
	public CatanModel resetGame(Session user) throws ServerException, GameInitializationException;
	public List<Command> getCommands(Session user) throws ServerException;
	public CatanModel executeCommands(Session user, List<Command> commands) throws ServerException, InvalidActionException;
	public void addAIPlayer(Session user, AIType type)throws ServerException;
	public List<AIType> getAITypes(Session user)throws ServerException;

	public CatanModel sendChat(Session user, String message)
			throws ServerException;
	public CatanModel rollDice(Session user, int number)
			throws ServerException, InvalidActionException;
	public CatanModel robPlayer(Session user,
			HexLocation newRobberLocation, PlayerReference victim)
					throws ServerException, InvalidActionException;
	public CatanModel buyDevCard(Session user)throws ServerException, InvalidActionException;
	public CatanModel yearOfPlenty(Session user, ResourceType type1, ResourceType type2)throws ServerException, InvalidActionException;
	public CatanModel roadBuilding(Session user, EdgeLocation road1, EdgeLocation road2)throws ServerException, InvalidActionException;
	public CatanModel soldier(Session user, 
			HexLocation newRobberLocation, PlayerReference victim)throws ServerException, InvalidActionException;
	public CatanModel monopoly(Session user, ResourceType type)throws ServerException, InvalidActionException;
	public CatanModel buildRoad(Session user, EdgeLocation location, boolean free)throws ServerException, InvalidActionException;
	public CatanModel buildSettlement(Session user, VertexLocation location, boolean free)throws ServerException, InvalidActionException;
	public CatanModel buildCity(Session user, VertexLocation location)throws ServerException, InvalidActionException;
	public CatanModel offerTrade(Session user, ResourceTradeList offer)throws ServerException, NotYourTurnException;
	public CatanModel respondToTrade(Session user, boolean accept)throws ServerException, TradeException;
	public CatanModel maritimeTrade(Session user,
			ResourceType inResource, ResourceType outResource, int ratio)throws ServerException, InvalidActionException;
	public CatanModel discardCards(Session user, ResourceList cards)throws ServerException, InvalidActionException;
	public CatanModel finishTurn(Session user)throws ServerException, InvalidActionException;
	
	public void changeLogLevel(LogLevel level) throws ServerException;
}
