import shared.definitions.*;
import shared.locations.*;

public class ClientFacade {

	/**
	*	@pre
	*	@post 
	*	@return true if the player can send chat	
	*	@return false otherwise
	 */
		boolean canSendChat() {
			return true;
		}
		
		boolean doSendChat() {
			return true;
		}
		/**
		 * @return true if it is the players turn, and 
		 * they haven't rolled already
		 * @return false otherwise
		 */
		boolean canRoll() {
			return true;
		}
		
		/**
		 * @param hexLoc The location on the map where the robber is to be placed
		 * @return true if the hex is not a desert hex.
		 * @return false otherwise
		 */
		boolean canRob(HexLocation hexLoc) {
			return true;
		}
		
		boolean doRob() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player has already rolled the die
		 * @return false otherwise
		 */
		boolean canFinishTurn() {
			return true;
		}
		
		boolean doFinishTurn() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player has at least one wool, one stone, and one wheat
		 * in their current hand.
		 * @return false if otherwise
		 */
		boolean canBuyDevelopmentCard() {
			return true;
		}
		
		boolean doBuyDevelopmentCard() {
			return true;
		}
		
		/**
		 * 
		 * @param edgeLoc The location (one of the 6 sides of one hex on the board)
		 *  where the road is to be placed.
		 * @return true if the given edge location is adjacent to at least one of
		 * the player's roads or municipalities (city or settlement), and that there is
		 * no currently placed road at that location.
		 * @return false otherwise
		 */
		boolean canBuildRoad(EdgeLocation edgeLoc) {
			return true;
		}
		
		boolean doBuildRoad() {
			return true;
		}
		
		/**
		 * 
		 * @param vertexLoc The location (one of the 6 vertices of one hex on the board)
		 * where the settlement is to be placed.
		 * @return true if the given vertex location is adjacent to at least one road
		 * that the player owns, the given location is empty, and the given location is 
		 * at least 2 vertices away from every other municipality (city/settlement)
		 * @return false otherwise
		 */
		boolean canBuildSettlement(VertexLocation vertexLoc) {
			return true;
		}
		
		boolean doBuildSettlement(VertexLocation vertexLoc) {
			return true;
		}
		
		/**
		 * 
		 * @param vertexLoc The location (one of the 6 vertices of one hex on the board)
		 * where the city is to be placed.
		 * @return true if the given vertex location is adjacent to at least one road
		 * that the player owns, the given location is empty, and the player owns a settlement
		 * at the given location.
		 * @return false otherwise
		 */
		boolean canBuildCity(VertexLocation vertexLoc) {
			return true;
		}
		/**
		 * 
		 * @param vertexLoc
		 * @return
		 */
		boolean doBuildCity(VertexLocation vertexLoc) {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one year of plenty card
		 * @return false otherwise
		 */
		boolean canYearOfPlenty() {
			return true;
		}
		
		boolean doYearOfPlenty() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one road building card,
		 * and has at least one unplaced road.
		 * @return false otherwise
		 */
		boolean canRoadBuildingCard() {
			return true;
		}
		
		boolean doRoadBuildCard() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the players owns at least one soldier card
		 * @return false otherwise
		 */
		boolean canSoldier() {
			return true;
		}
		
		boolean doSoldier() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one monopoly card
		 * @return false if player owns zero monopoly cards
		 */
		boolean canMonopoly() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doMonopoly() {
			return true;
		}
		
		/**
		 * 
		 * @return true if the player owns at least one monument card
		 * @return false otherwise
		 */
		boolean canMonument() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doMonument() {
			return true;
		}
		
		/**
		 * 
		 * @return true if it is your turn and you have sufficient cards
		 * @return false otherwise
		 */
		boolean canOfferTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doOfferTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return true if player has enough cards
		 * @return false otherwise
		 */
		boolean canAcceptTrade() {
			
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doAcceptTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return true if player has a settlement or city on a port.
		 * @return false otherwise
		 */
		boolean canMaritimeTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doMaritimeTrade() {
			return true;
		}
		
		/**
		 * 
		 * @return true if seven is rolled and amount of cards in hand is over seven
		 * @return false otherwise
		 */
		boolean canDiscardCards() {
			return true;
		}
		
		/**
		 * 
		 * @return
		 */
		boolean doDiscardCards() {
			return true;
		}
}
