package com.group4.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.group4.controller.GameController.Difficulty;
import com.group4.controller.GameController.GameState;
import com.group4.controller.GameController.GameType;
import com.group4.observers.Observable;
import com.group4.observers.Observer;
import com.group4.util.GameProperty;
import com.group4.util.Player;

public class GameOptions implements Observable {
	
	// List of all the observers watching this model.
	private ArrayList<Observer> gameObservers;

	// Difficulty holding enum value
	private Difficulty difficulty;
	
	// GameType holding enum value
	private GameType gameType;
	
	// GameProperty instance holding the game logic
	private GameProperty game;
	
	// Board instance, holds the board with tiles
	private Board board;
	
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
     * @author Jasper van der Kooi
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
     * Create a new game with given difficulty + gametype
     * 
     * @param difficulty
     * @param gameType
     */
	public GameOptions(Difficulty difficulty, GameType gameType) {
		
		this.difficulty = difficulty;
		this.gameType = gameType;
		
		// Initializing gameObservers with an empty ArrayList to contain Observer objects.
		this.gameObservers = new ArrayList<Observer>();
		
		// Create the game
		this.game = this.instantiate("com.group4.games." + gameType.toString().toUpperCase(), GameProperty.class);
		
		// Create players
		for(int index = 0; index < game.getPlayerAmount(); index++) {
			this.players.put("p" + index, new Player("p" + index, this.game));
		}
		
		// Create board
		this.board = new Board(this.game.getBoardHeight(), this.game.getBoardWidth());
		
	}
	
	/***
	 * Get Game Difficulty
	 * 
	 * @return Difficulty
	 */
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	/***
	 * Get Game type
	 * 
	 * @return GameType
	 */
	public GameType getGameType() {
		return this.gameType;
	}
	
	/***
	 * Get the GameState
	 * 
	 * @return GameState - The state the game is in
	 */
	public GameState getGameState() {
		return this.gameState;
	}
	
	/***
	 * Get the board for the current game
	 * 
	 * @return Board - The board currently in use by the game
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/***
	 * Set the GameState
	 * 
	 * @param state - The state to set the game to
	 */
	public void setGameState(GameState state) {
		this.gameState = state;
	}
	
	/***
	 * Get Player
	 * 
	 * @param id - String playerId
	 * @return Player - The player found, or NULL
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
	 */
	public HashMap<String, Player> getPlayers(){
		return this.players;
	}

	/**
	 * Method that adds observers to the gameObservers list.
	 */
	@Override
	public void registerObserver(Observer observer) {
		this.gameObservers.add(observer);
		
	}

	/**
	 * Method that removes observers from the gameObservers list.
	 */
	@Override
	public void removeObserver(Observer observer) {
		this.gameObservers.remove(observer);
		
	}

	/**
	 * Method that notifies all the observers in the gameObserver list that there is an update.
	 */
	@Override
	public void notifyObservers() {
		for(Observer observer : gameObservers) {
			observer.update();
		}
	}
	
}
