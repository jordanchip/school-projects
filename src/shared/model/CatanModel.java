package shared.model;

import java.util.List;

public class CatanModel {
	private MessageList chat;
	private MessageList log;
	private Board map;
	private TradeOffer tradeOffer;
	private TurnTracker turnTracker;
	private Bank bank;
	private List<Player> players;
	private PlayerReference longestRoad;
	private PlayerReference largestArmy;
	private PlayerReference winner;
	
	private int version;

	public CatanModel() {
		
	}

	/**
	 * @return the tradeOffer
	 */
	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	/**
	 * @param tradeOffer the tradeOffer to set
	 */
	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}

	/**
	 * @return the longestRoad
	 */
	public PlayerReference getLongestRoad() {
		return longestRoad;
	}

	/**
	 * @param longestRoad the longestRoad to set
	 */
	public void setLongestRoad(PlayerReference longestRoad) {
		this.longestRoad = longestRoad;
	}

	/**
	 * @return the largestArmy
	 */
	public PlayerReference getLargestArmy() {
		return largestArmy;
	}

	/**
	 * @param largestArmy the largestArmy to set
	 */
	public void setLargestArmy(PlayerReference largestArmy) {
		this.largestArmy = largestArmy;
	}

	/**
	 * @return the chat
	 */
	public MessageList getChat() {
		return chat;
	}

	/**
	 * @return the log
	 */
	public MessageList getLog() {
		return log;
	}

	/**
	 * @return the map
	 */
	public Board getMap() {
		return map;
	}

	/**
	 * @return the turnTracker
	 */
	public TurnTracker getTurnTracker() {
		return turnTracker;
	}

	/**
	 * @return the bank
	 */
	public Bank getBank() {
		return bank;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the winner
	 */
	public PlayerReference getWinner() {
		return winner;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
	
	
}
