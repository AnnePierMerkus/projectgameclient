package com.group4.util;

import java.util.HashMap;
import java.util.List;

/***
 * GameProperty interface
 * 
 * Holds all methods every game class should have
 * 
 * @author Gerwin Terpstra & Jasper van der Kooi
 *
 */
public abstract class GameProperty {

	protected HashMap<String, String> displayNames = new HashMap<String, String>();
	
	/***
	 * For every player id, set a display name in the constructor of every game
	 * E.g. HashMap.put("p1", "White"); HashMap.put("p2", "Black");
	 * 
	 * @return String - Display name for given playerId
	 * @author GRTerpstra & mobieljoy12
	 */
	public String getDiplayName(String playerId) {
		return (this.displayNames.containsKey(playerId)) ? this.displayNames.get(playerId) : "";
	}
	
	/***
	 * Get the amount of players for the game
	 * 
	 * @return Amount of players
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract int getPlayerAmount();
	
	/***
	 * The width of the board in number of columns
	 * 
	 * @return Number of columns
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract int getBoardWidth();
	
	/***
	 * The height of the board in number of columns
	 * 
	 * @return Number of columns
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract int getBoardHeight();
	
	/***
	 * The player that should start the game
	 * Set to -1 if player should be chosen at random
	 * Use 0 if first listed player should start, 1 if the second etc.
	 * So 0 would mean p1 starts the game
	 * 
	 * @return Player index that should start
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract int playerStart();
	
	/***
	 * Get available options for a given player
	 * 
	 * @param player - The player to give options for
	 * @return List<Tile> - List of Tile options player could play
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract List<Tile> getAvailableOptions(Player player);
	
	/***
	 * Make a move on a given Tile for a given Player
	 * 
	 * @param tile - The tile to make a move on
	 * @param player - The Player to make the move for
	 * @return boolean - Whether move was legal
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract boolean makeMove(Tile tile, Player player);
	
	/***
	 * Check if a move on a Tile is legal for a given Player
	 * 
	 * @param tile - The tile to make the move on
	 * @param player - The Player to make the move with
	 * @return boolean - Whether the move is legal
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract boolean isLegalMove(Tile tile, Player player);
	
	/***
	 * Whether the game has ended or should continue
	 * 
	 * @return boolean - Whether the game has ended
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract boolean gameHasEnded();
	
}
