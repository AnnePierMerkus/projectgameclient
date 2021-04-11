package com.group4.util;

import java.util.HashMap;
import java.util.List;

import com.group4.controller.GameController.GameType;
import com.group4.model.Board;
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

	// Hold the display names for the players
	protected HashMap<String, String> displayNames = new HashMap<String, String>();
	
	// Hold the connection to the gameoptions
	protected GameOptions game = null;
	
	// Whether the game is on matchpoint
	private boolean matchPoint = false;
	
	// Whether the game has ended
	private boolean gameEnded = false;
	
	// The player that has won the game, null for when it is a tie
	protected Player playerWon = null;
	
	/***
	 * Set the matchpoint variable
	 * 
	 * @param matchpoint
	 * @author mobieljoy12
	 */
	public void setMatchPoint(boolean matchpoint) {
		this.matchPoint = matchpoint;
	}
	
	/***
	 * Check whether it's currently a matchpoint
	 * 
	 * @return boolean - Matchpoint
	 */
	public boolean isMatchPoint() {
		return this.matchPoint;
	}
	
	/***
	 * End the game
	 * 
	 * @author mobieljoy12
	 */
	public void endGame() {
		//TODO remove later
		System.out.println("Game has ended");
		if(this.playerWon == null) {
			
		}
		this.gameEnded = true;
	}
	
	/***
	 * Get the player that won the game
	 * Null if player no player has won (yet)
	 * 
	 * @return Player - The player that won
	 */
	public Player getPlayerWon() {
		return this.playerWon;
	}
	
	/***
	 * Whether the game has ended or should continue
	 * 
	 * @return boolean - Whether the game has ended
	 * @author GRTerpstra & mobieljoy12
	 */
	public boolean gameHasEnded() {
		return this.gameEnded;
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
	 * Get the GameType that is currently running
	 * 
	 * @return GameType - The GameType that is running
	 */
	public abstract GameType getGameType();
	
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
	 * 
	 * @param String - The player who gets first turn
	 */
	public abstract void doSetup(String currentPlayerTurn);
	
	/***
	 * The player that should start the game
	 * Set to empty string if player should be chosen at random
	 * Use p1 if first listed player should start, p2 if the second etc.
	 * 
	 * @return String - Player that should start
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract String playerStart();
	
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
	 * Check if the player has met the criteria to end the game
	 * 
	 * @param player - The player to check for
	 * @return boolean - Met criteria
	 * @author GRTerpstra
	 */
	public abstract boolean endGameFlagMet(Player player);
	
	/***
	 * Decide which player wins the game, null if it is a tie
	 * 
	 * @author GRTerpstra & mobieljoy12
	 */
	public abstract void decidePlayerWin();
		
}
