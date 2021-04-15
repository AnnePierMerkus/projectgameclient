package com.group4.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.group4.controller.GameController.GameType;
import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;
import com.group4.util.observers.TileObserver;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * @author GRTerpstra & Anne Pier Merkus
 */
public class Tile implements Observable {
	/**
	 * Index of the tile on the board.
	 */
	private int index;

	/**
	 * Weight of this tile depending on the position on the board.
	 */
	private int weight = 0;

	/**
	 * Player occupying this tile.
	 */
	private Player playerOnTile = null;

	/**
	 * List of observers to call when the occupant of the tile changes.
	 */
	private ArrayList<TileObserver> observers = new ArrayList<TileObserver>();

	/***
	 * Make a new Tile and set the relevant values.
	 * 
	 * @param index index of this tile.
	 * @param weight weight for this tile
	 * @author GRTerpstra & Anne Pier Merkus
	 */
	public Tile(int index, int weight) {
		this.index = index;
		this.weight = weight;

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
	 * Get weight for this Tile
	 *
	 * @return int - weight
	 * @author AnnePierMerkus
	 */
	public int getWeight() {
		return this.weight;
	}

	/***
	 * Get the Player on this Tile
	 * Returns null if there is no player
	 * 
	 * @return Player - The player on this tile
	 * @author mobieljoy12
	 */
	public Player getOccupant() {
		return this.playerOnTile;
	}

	public void setOccupant(Player occupant, int threadId) {
		this.playerOnTile = occupant;
		this.updateFilledTiles(threadId);
	}

	/***
	 * Make this tile unoccupied
	 * 
	 * @author mobieljoy12
	 */
	public void reset() {
		this.playerOnTile = null;
	}
	
	/***
	 * Check if the Tile is occupied
	 * 
	 * @return boolean - Occupied
	 * @author mobieljoy12
	 */
	public boolean isOccupied() {
		return (this.playerOnTile != null);
	}

	public Player getPlayerOnTile() {
		return playerOnTile;
	}
	/**
	 * Register TileObservers for this Tile.
	 * @param observer Observer to be added.
	 */
	@Override
	public void registerObserver(Observer observer) {
		TileObserver tileObserver = (TileObserver) observer;
		this.observers.add(tileObserver);
	}

	/**
	 * Remove observers from this Tile.
	 * @param observer Observer to be removed.
	 */
	@Override
	public void removeObserver(Observer observer) {
		this.observers.remove(observer);
	}

	/**
	 * Notifying all the observers when the occupant changes.
	 */
	@Override
	public void notifyObservers() {
		this.observers.forEach((o) -> o.update(this));
	}
	
	/***
	 * Update the filled tiles list
	 * 
	 * @param threadId - The thread to update the list for
	 */
	public void updateFilledTiles(int threadId) {
		this.observers.forEach((o) -> o.updateFilledTile(this, threadId));
	}
	
}
