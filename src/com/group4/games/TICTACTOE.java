package com.group4.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.group4.controller.GameController.GameType;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

/**
 * The TICTACTIE CLASS defines a Tictactoe game.
 * Extends GameProperty.
 * 
 * @author Mobieljoy12 & GRTerpstra
 * @version 1.0
 * @since   2021-03-25
 */
public class TICTACTOE extends GameProperty {
	
	/**
	 * The constructor of the TICTACTOE class.
	 * This constructor instantiates the display names for the players.
	 * @author mobieljoy12.
	 */
	public TICTACTOE() {
		this.displayNames.put("p1", "X");
		this.displayNames.put("p2", "O");
	}

	/***
	 * Get the GameType that is currently running
	 * 
	 * @return GameType - The GameType that is running
	 * @author mobieljoy12.
	 */
	@Override
	public GameType getGameType() {
		return GameType.TICTACTOE;
	}
	
	/***
	 * The width of the board in number of columns.
	 * 
	 * @return int - Number of columns.
	 * @author GRTerpstra & mobieljoy12.
	 */
	@Override
	public int getBoardWidth() {
		return 3;
	}

	/***
	 * The height of the board in number of columns.
	 * 
	 * @return int - Number of columns.
	 * @author GRTerpstra & mobieljoy12.
	 */
	@Override
	public int getBoardHeight() {
		return 3;
	}
	
	@Override
	public void doSetup(String currentPlayerSetup) {
		
	}	
	
	/***
	 * This method returns the player that should start the game
	 * 
	 * @return String - P1 (X) is the player that should start the game.
	 * @author GRTerpstra & mobieljoy12
	 */
	@Override
	public String playerStart() {
		return "p1"; // X always starts
	}

	/**
	 * The getAvailableOptions method calculates all the moves the given player can make.
	 * @param player - The player whose available moves should be calculates.
	 * @return ArrayList<Tile> - List of available moves.
	 * @author GRTerpstra.
	 */
	@Override
	public List<Tile> getAvailableOptions(Player player) {
		ArrayList<Tile> availableOptions = new ArrayList<Tile>();
		HashMap<Integer, Tile> board = this.game.getBoard().getGameBoard();
		for(Tile tile : board.values()) {
			if(tile.getOccupant() == null) {
				availableOptions.add(tile);
			}
		}
		return availableOptions;
	}

	/**
	 * the makeMove method implements all the changes made to the board after checking if the move is legal.
	 * @param tile - The tile on which the move should be made.
	 * @param player - the player who makes the move.
	 * @return boolean - true if the move has been made, false otherwise.
	 * @author GRTerpstra.
	 */
	@Override
	public boolean makeMove(Tile tile, Player player) {
		this.gameHasEnded();
		if(this.checkGameEnded()) return false;
		if(this.isLegalMove(tile, player)) {
			this.game.getBoard().savePrevious(tile, tile.getOccupant());
			tile.setOccupant(player);
			this.game.getBoard().incMoveCounter();
			this.gameHasEnded();
			return true;
		}
		return false;
	}

	/**
	 * the isLegalMove method checks if the move that is about to happen is legal.
	 * @param tile - The tile on which the move should be made.
	 * @param player - the player who makes the move.
	 * @return boolean - true if the move is legal, false otherwise.
	 * @author GRTerpstra.
	 */
	@Override
	public boolean isLegalMove(Tile tile, Player player) {
		List<Tile> availableOptions = this.getAvailableOptions(player);
		player.setAvailableOptions(availableOptions);
		if(availableOptions.isEmpty()) {
			return false;
		}
		else if(availableOptions.contains(tile)) {
			return true;
		}
		return false;
	}
	
	/**
	 * the gameHasEnded method checks if the current game should terminate.
	 * This method also changes the playerWon variable accordingly.
	 * @return boolean - true if the game should end, false otherwise.
	 * @author GRTerpstra.
	 */
	@Override
	public boolean gameHasEnded() {
		for(Player player : PlayerList.players.values()) {
			ArrayList<Integer> playerTiles = new ArrayList<Integer>();
			for(Tile tile : this.game.getBoard().getGameBoard().values()) {
				if(tile.getOccupant() == player) {
					playerTiles.add(tile.getIndex());
				}
			}
			if((playerTiles.contains(0) && playerTiles.contains(1) && playerTiles.contains(2)) ||
			   (playerTiles.contains(3) && playerTiles.contains(4) && playerTiles.contains(5)) ||
			   (playerTiles.contains(6) && playerTiles.contains(7) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(3) && playerTiles.contains(6)) ||
			   (playerTiles.contains(1) && playerTiles.contains(4) && playerTiles.contains(7)) ||
			   (playerTiles.contains(2) && playerTiles.contains(5) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(4) && playerTiles.contains(8)) ||
			   (playerTiles.contains(2) && playerTiles.contains(4) && playerTiles.contains(6))
			  ) {
				this.gameEnded = true;
				return true;
			}
			if(this.getAvailableOptions(player).isEmpty()) {
				this.gameEnded = true;
				return true;
			}
		}
		this.gameEnded = false;
		return false;
	}

	/**
	 * the getPlayerWon method returns the player who has won the game.
	 * @return Player - the player who has won the game.
	 * @author GRTerpstra.
	 */
	@Override
	public Player getPlayerWon() {
		Player playerWin = null;
		
		for(Player player : PlayerList.players.values()) {
			ArrayList<Integer> playerTiles = new ArrayList<Integer>();
			for(Tile tile : this.game.getBoard().getGameBoard().values()) {
				if(tile.getOccupant() == player) {
					playerTiles.add(tile.getIndex());
				}
			}
			if((playerTiles.contains(0) && playerTiles.contains(1) && playerTiles.contains(2)) ||
			   (playerTiles.contains(3) && playerTiles.contains(4) && playerTiles.contains(5)) ||
			   (playerTiles.contains(6) && playerTiles.contains(7) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(3) && playerTiles.contains(6)) ||
			   (playerTiles.contains(1) && playerTiles.contains(4) && playerTiles.contains(7)) ||
			   (playerTiles.contains(2) && playerTiles.contains(5) && playerTiles.contains(8)) ||
			   (playerTiles.contains(0) && playerTiles.contains(4) && playerTiles.contains(8)) ||
			   (playerTiles.contains(2) && playerTiles.contains(4) && playerTiles.contains(6))
			  ) {
				playerWin = player;
			}
		}
		
		return playerWin;
	}

}
