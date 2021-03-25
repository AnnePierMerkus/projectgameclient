package com.group4.util;

public class Player {

	// String because player id's should be formatted as: "p{id}"
	// E.g. p1, p2, p3 etc.
	private String id;
	
	// Gameproperty instance so player can reach the game object
	private GameProperty gameProperty;
	
	/***
	 * Create a new Player
	 * 
	 * @param id
	 * @param gameProperty
	 */
	public Player(String id, GameProperty gameProperty) {
		this.id = id;
		this.gameProperty = gameProperty;
	}
	
	public String getId() {
		return this.id;
	}
	
}
