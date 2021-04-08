package com.group4.util;

import java.util.HashMap;
import java.util.List;

import com.group4.model.GameOptions;

/***
 * GameProperty interface
 * 
 * Holds all methods every game class should have
 * TODO - Add exceptions for when gameoptions not set
 * 
 * @author GRTerpstra & mobieljoy12
 *
 */
public abstract class GameProperty {

	protected HashMap<String, String> displayNames = new HashMap<String, String>();
	protected GameOptions game = null;

	//TODO remove later
	public void tempDisplayBoard() {
		for(int row = 0; row < this.game.getBoard().getHeight(); row++) {
			for(int col = 0; col < this.game.getBoard().getWidth(); col++) {
				// ((row * getRowWidth()) + column)
				int tileIndex = (row * this.game.getBoard().getWidth()) + col;
				String id = (this.game.getBoard().getTile(tileIndex).getOccupant() == null) ? "" : this.game.getBoard().getTile((row * this.game.getBoard().getWidth()) + col).getOccupant().getId();
				System.out.print(" [" + id + "] ");
			}
			System.out.println();
		}

	}
	
	/***
	 * Set the Gameoptions
	 * 
	 * @param gameOptions
	 * @author mobieljoy12
	 */
	public void setGameOptions(GameOptions gameOptions) {
		this.game = gameOptions;
	}
	
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
	 * Method will be called before the match starts and after the board is made
	 * E.g. for setting tiles
	 */
	public abstract void doSetup();
	
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
