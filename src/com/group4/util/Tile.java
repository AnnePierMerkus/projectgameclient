package com.group4.util;

import java.util.ArrayList;

import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

import javafx.scene.layout.StackPane;

public class Tile extends StackPane implements Observable {
	
	private int index;
	
	private Player playerOnTile = null;
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/***
	 * Make a new Tile
	 * 
	 * @param rowCoordinate - Row coordinate
	 * @param columnCoordinate - Column coordinate
	 * @author GRTerpstra
	 */
	public Tile(int index) {
		this.index = index;
	}
	
	/***
	 * Get Index for this Tile
	 * 
	 * @return int - Index
	 * @author GRTerpstra
	 */
	public int getIndex() {
		return this.index;
	}
	
	/***
	 * Get the Player on this Tile
	 * Returns null if there is no player
	 * 
	 * @return Player or null
	 * @author mobieljoy12
	 */
	public Player getOccupant() {
		return this.playerOnTile;
	}
	
	/***
	 * Set the Player on this Tile
	 * 
	 * @param occupant - Player to set on this Tile
	 * @author mobieljoy12
	 */
	public void setOccupant(Player occupant) {
		this.playerOnTile = occupant;
		this.notifyObservers();
	}
	
	/***
	 * Make this tile unoccupied
	 * @author mobieljoy12
	 */
	public void reset() {
		this.playerOnTile = null;
		this.notifyObservers();
	}
	
	/***
	 * Check if the Tile is occupied
	 * 
	 * @return boolean - Whether Tile is occupied
	 * @author mobieljoy12
	 */
	public boolean isOccupied() {
		return (this.playerOnTile != null);
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
		this.observers.forEach((o) -> o.update(null));
	}
	
}
