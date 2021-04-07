package com.group4.util;

import java.util.List;

public class Player {

	// String because player id's should be formatted as: "p{id}"
	// E.g. p1, p2, p3 etc.
	private String id;
	
	// Gameproperty instance so player can reach the game object
	private GameProperty gameProperty = null;
	
	/***
	 * Create a new Player
	 * 
	 * @param id
	 * @param gameProperty
	 * @author mobieljoy12
	 */
	public Player(String id) {
		this.id = id;
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
	 * Make a move on the board on a given Tile
	 * Returns false if player is not in a game
	 * 
	 * @param tile - The Tile to make a move on
	 * @return boolean - Whether the move was legal
	 * @author mobieljoy12
	 */
	public boolean makeMove(Tile tile) {
		if(this.gameProperty == null) {
			//TODO - Catch exception, player is not in a game so he can not make a move
			return false;
		}
		return (this.gameProperty.isLegalMove(tile, this)) ? this.gameProperty.makeMove(tile, this) : false;
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
			return null;
		}
		return this.gameProperty.getAvailableOptions(this);
	}
	
	/***
	 * Set a GameProperty to a player when he joins a game
	 * 
	 * @param game - GameProperty instance
	 */
	public void setGameProperty(GameProperty game) {
		this.gameProperty = game;
	}
	
}
