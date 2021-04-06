package com.group4.util;

import javafx.scene.layout.StackPane;

public class Tile extends StackPane {
	
	private int index;
	
	private Player playerOnTile = null;
	
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
	public int getRowCoordinate() {
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
	}
	
	/***
	 * Make this tile unoccupied
	 * @author mobieljoy12
	 */
	public void reset() {
		this.playerOnTile = null;
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
	
}
