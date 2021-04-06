package com.group4.model;

import java.util.HashMap;

import com.group4.controller.GameController.Difficulty;
import com.group4.controller.GameController.GameState;
import com.group4.controller.GameController.GameType;
import com.group4.util.BoardObserver;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.network.Client;
import com.group4.util.network.NetworkPlayer;
import com.group4.util.observers.Observer;

public class GameOptions {
	
	// Difficulty holding enum value
	private Difficulty difficulty;
	
	// GameType holding enum value
	private GameType gameType;
	
	// GameProperty instance holding the game logic
	private GameProperty game;
	
	// Board instance, holds the board with tiles
	private Board board;
	
	// The boardobserver
	private BoardObserver boardObserver = new BoardObserver(this);
	
	// Save the players into a map with Id => Player object
	private HashMap<String, Player> players = new HashMap<String, Player>();
	
	// Set the default gameState to preparing
	private GameState gameState = GameState.PREPARING;
	
	/**
     * Instanciate GameProperty class for the gametype
     *
     * @param className
     * @param type
     * @return GameProperty subclass
     * @author mobieljoy12
     */
    @SuppressWarnings("deprecation")
    private GameProperty instantiate(final String className, @SuppressWarnings("rawtypes") final Class type) {
    	try {
            return (GameProperty) type.cast(Class.forName(className).newInstance());
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
	
    /***
     * Create a new singleplayer game with given difficulty + gametype
     * 
     * @param difficulty
     * @param gameType
     * @author mobieljoy12
     */
	public GameOptions(Difficulty difficulty, GameType gameType) {
		
		this.difficulty = difficulty;
		this.gameType = gameType;
		
		// Create the game
		this.game = this.instantiate("com.group4.games." + gameType.toString().toUpperCase(), GameProperty.class);
		
		for(int playerCount = 1; playerCount < 3; playerCount++) {
			this.players.put("p" + playerCount, new Player("p" + playerCount, this.game));
		}
		
		// Create board
		this.board = new Board(this.game.getBoardHeight(), this.game.getBoardWidth());
		
	}
    
    /***
     * Create a new multiplayer game with given difficulty + gametype
     * 
     * @param difficulty
     * @param gameType
     * @author mobieljoy12
     */
	public GameOptions(Difficulty difficulty, GameType gameType, Client client) {
		
		this.difficulty = difficulty;
		this.gameType = gameType;
		
		// Create the game
		this.game = this.instantiate("com.group4.games." + gameType.toString().toUpperCase(), GameProperty.class);
		
		// Create players, if multiplayer, networkplayer is p1
		// Get network player via getPlayer("p1")
		this.players.put("p1", new NetworkPlayer("p1", this.game, client));
		this.players.put("p2", new Player("p2", this.game));
		
		// Create board
		this.board = new Board(this.game.getBoardHeight(), this.game.getBoardWidth());
		
	}
	
	/***
	 * Get Game Difficulty
	 * 
	 * @return Difficulty
	 * @author mobieljoy12
	 */
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	/***
	 * Get Game type
	 * 
	 * @return GameType
	 * @author mobieljoy12
	 */
	public GameType getGameType() {
		return this.gameType;
	}
	
	/***
	 * Get the GameState
	 * 
	 * @return GameState - The state the game is in
	 * @author mobieljoy12
	 */
	public GameState getGameState() {
		return this.gameState;
	}
	
	/***
	 * Get the board for the current game
	 * 
	 * @return Board - The board currently in use by the game
	 * @author mobieljoy12
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/***
	 * Set the GameState
	 * 
	 * @param state - The state to set the game to
	 * @author mobieljoy12
	 */
	public void setGameState(GameState state) {
		this.gameState = state;
	}
	
	/***
	 * Get Player
	 * If multiplayer, p1 can be cast to network player
	 * E.g. NetworkPlayer networkPlayer = (NetworkPlayer) GameOptions.getPlayer("p1");
	 * 
	 * @param id - String playerId
	 * @return Player - The player found, or NULL
	 * @author mobieljoy12
	 */
	public Player getPlayer(String id) {
		if(this.players.containsKey(id)) {
			return this.players.get(id);
		}
		return null;
	}
	
	/***
	 * Get all players
	 * 
	 * @return HashMap<String, Player> - HashMap with all players with id
	 * @author mobieljoy12
	 */
	public HashMap<String, Player> getPlayers(){
		return this.players;
	}
}
