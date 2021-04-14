package com.group4.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

public class Player implements Observable {

	public enum PlayerState {
		WAITING, PLAYING_NO_TURN, PLAYING_HAS_TURN	
	};
	
	// String because player id's should be formatted as: "p{id}"
	// E.g. p1, p2, p3 etc.
	private String id;
	
	// Gameproperty instance so player can reach the game object
	protected GameProperty gameProperty = null;
	
	// Holding the state the player is currently in
	protected PlayerState playerState;
	
	private HashMap<Integer, ArrayList<Tile>> lastAvailableOptions = new HashMap<Integer, ArrayList<Tile>>();
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/***
	 * Create a new Player
	 * 
	 * @param id
	 * @author mobieljoy12
	 */
	public Player(String id) {
		this.id = id;
		this.playerState = PlayerState.WAITING;
	}

	/***
	 * Get player id
	 * 
	 * @return String - Player id
	 * @author mobieljoy12
	 */
	public String getId() {
		return this.id;
	}
	
	/***
	 * Get the PlayerState
	 * 
	 * @return PlayerState - The state the player is currently in
	 * @author mobieljoy12
	 */
	public PlayerState getPlayerState() {
		return this.playerState;
	}
	
	/***
	 * Set the player state
	 * 
	 * @param threadId - The thread to make the player move on
	 * @param state - The state to set the player to
	 * @author mobieljoy12
	 */
	public void setPlayerState(PlayerState state, int threadId) {
		this.playerState = state;
		if(state.equals(PlayerState.PLAYING_HAS_TURN)) {
			if(this.gameProperty != null) { // If game is ready
				if(this.getAvailableOptions(threadId).isEmpty()) { // Check if player has turns left
					if(!this.gameProperty.gameHasEnded(threadId)) { // If game hasn't ended
						System.out.println("Giving turn back to other player");
						this.notifyObservers(); // Give turn back
					}
				}
			}
		}
	}
	
	/***
	 * Make a move on the board on a given Tile
	 * Returns false if player is not in a game
	 * 
	 * @param tile - The Tile to make a move on
	 * @return boolean - Whether the move was legal
	 * @author mobieljoy12
	 */
	public void makeMove(Tile tile, int threadId) {
		//TODO check matchpoint & end game
		// Don't allow moves if player does not have the turn
		if(this.playerState != PlayerState.PLAYING_HAS_TURN) return;
		if(this.gameProperty.checkGameEnded(threadId)) return;
		
		if(this.gameProperty.makeMove(tile, this, threadId)){
			this.notifyObservers();
		}
	}
	
	/***
	 * Check if player has moves left
	 * 
	 * @return boolean - Moves left
	 */
	public boolean hasMovesLeft() {
		return !this.lastAvailableOptions.isEmpty();
	}
	
	/***
	 * Set the last available options so the player is up-to-date
	 * 
	 * @param options - Options
	 * @param threadId - The thread to get the options for
	 */
	public void setAvailableOptions(List<Tile> options, int threadId) {
		
		if(!this.lastAvailableOptions.containsKey(threadId)) this.lastAvailableOptions.put(threadId, new ArrayList<Tile>());
		
		this.lastAvailableOptions.put(threadId, new ArrayList<Tile>(options));
	}
	
	/***
	 * Get available Tiles to make a move on
	 * Returns null if player is not in a game
	 * 
	 * @param threadId - The thread to get the options for
	 * @return List<Tile> - List of available Tiles to make a move on
	 * @author mobieljoy12
	 */
	public List<Tile> getAvailableOptions(int threadId){
		if(this.gameProperty == null) {
			System.out.println("Player tried getting options while gameproperty doesn't exist");
			return null;
		}
		List<Tile> options = this.gameProperty.getAvailableOptions(this,threadId);
		this.setAvailableOptions(options, threadId);
		return options;
	}
	
	/***
	 * Set a GameProperty to a player when he joins a game
	 * 
	 * @param game - GameProperty instance
	 * @author mobieljoy12
	 */
	public void setGameProperty(GameProperty game) {
		this.gameProperty = game;
		this.getAvailableOptions(0);
	}

	@Override
	public void registerObserver(Observer observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers() {
		this.observers.forEach((o) -> o.update(this));
	}
	
}
