package com.group4.util;

import javafx.scene.layout.StackPane;

public class Tile extends StackPane {
	
	private int rowCoordinate;
	private int columnCoordinate;
	
	private Player playerOnTile = null;
	
	/***
	 * Make a new Tile
	 * 
	 * @param rowCoordinate - Row coordinate
	 * @param columnCoordinate - Column coordinate
	 * @author GRTerpstra
	 */
	public Tile(int rowCoordinate, int columnCoordinate) {
		this.rowCoordinate = rowCoordinate;
		this.columnCoordinate = columnCoordinate;
	}
	
	/***
	 * Get Row coordinate for this Tile
	 * 
	 * @return int - Row
	 * @author GRTerpstra
	 */
	public int getRowCoordinate() {
		return this.rowCoordinate;
	}
	
	/***
	 * Get Column coordinate for this Tile
	 * 
	 * @return int - Column
	 * @author GRTerpstra
	 */
	public int getColumnCoordinate() {
		return this.columnCoordinate;
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
