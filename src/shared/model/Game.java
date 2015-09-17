package shared.model;

import java.util.List;

public class Game {
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

	public Game() {
		
	}
}
