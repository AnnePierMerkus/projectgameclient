package com.group4.util;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.observers.Observer;
import com.group4.util.observers.Observable;

public class Player implements Observable {

	// String because player id's should be formatted as: "p{id}"
	// E.g. p1, p2, p3 etc.
	private String id;
	
	// Gameproperty instance so player can reach the game object
	private GameProperty gameProperty;
	
	// List with Observers to update
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/***
	 * Create a new Player
	 * 
	 * @param id
	 * @param gameProperty
	 * @author mobieljoy12
	 */
	public Player(String id, GameProperty gameProperty) {
		this.id = id;
		this.gameProperty = gameProperty;
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
	 * 
	 * @param tile - The Tile to make a move on
	 * @return boolean - Whether the move was legal
	 * @author mobieljoy12
	 */
	public boolean makeMove(Tile tile) {
		boolean result = this.gameProperty.makeMove(tile, this);
		this.notifyObservers();
		return result;
	}
	
	/***
	 * Get available Tiles to make a move on
	 * 
	 * @return List<Tile> - List of available Tiles to make a move on
	 * @author mobieljoy12
	 */
	public List<Tile> getAvailableOptions(){
		return this.gameProperty.getAvailableOptions(this);
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
