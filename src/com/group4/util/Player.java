package com.group4.util;

import java.util.ArrayList;
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
	 * @param state - The state to set the player to
	 * @author mobieljoy12
	 */
	public void setPlayerState(PlayerState state) {
		this.playerState = state;
	}
	
	/***
	 * Make a move on the board on a given Tile
	 * Returns false if player is not in a game
	 * 
	 * @param tile - The Tile to make a move on
	 * @return boolean - Whether the move was legal
	 * @author mobieljoy12
	 */
	public void makeMove(Tile tile) {
		//TODO check matchpoint & end game
		// Don't allow moves if player does not have the turn
		if(this.playerState != PlayerState.PLAYING_HAS_TURN) return;
		if(this.gameProperty.gameHasEnded()) return;
		
		if(this.gameProperty.makeMove(tile, this)){
			this.notifyObservers();
		}
	}
	
	/***
	 * Get available Tiles to make a move on
	 * Returns null if player is not in a game
	 * 
	 * @return List<Tile> - List of available Tiles to make a move on
	 * @author mobieljoy12
	 */
	public List<Tile> getAvailableOptions(){
		if(this.gameProperty == null) {
			//TODO - Catch exception, player is not in a game so he can not get available options
			System.out.println("Player tried getting options while gameproperty doesn't exist");
			return null;
		}
		return this.gameProperty.getAvailableOptions(this);
	}
	
	/***
	 * Set a GameProperty to a player when he joins a game
	 * 
	 * @param game - GameProperty instance
	 * @author mobieljoy12
	 */
	public void setGameProperty(GameProperty game) {
		this.gameProperty = game;
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
