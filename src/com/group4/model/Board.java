package com.group4.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.TileUI;
import com.group4.util.observers.TileObserver;

import static com.group4.games.REVERSI.getDirectionOffset;

/**
 * Class that creates and defines the game board.
 */
public class Board {

	private HashMap<Integer, TileUI> gameBoard = new HashMap<Integer, TileUI>();
	private HashMap<String, HashMap<Integer, TileUI>> filledTiles = new HashMap<String, HashMap<Integer, TileUI>>();
	//private HashMap<Integer, HashMap<Integer, Tile>> previousBoard = new HashMap<Integer, HashMap<Integer, Tile>>();
	private int moveCounter = 0;
	private int height;
	private int width;
	
	/***
	 * Decrement the move counter
	 * 
	 * @author mobieljoy12
	 */
	private void decMoveCounter() {
		if(this.moveCounter > 0) this.moveCounter--;
	}

	private Board(HashMap<Integer, TileUI> gameBoard, int height, int width) {
		this(height, width);
		this.gameBoard = gameBoard;
	}
	/**
	 * Method that creates a new board which size is defined by the given height and width.
	 * 
	 * @param height the number of rows the board has to contain.
	 * @param width the number of columns the board has to contain.
	 * @author GRTerpstra
	 */
	public Board(int height, int width) {
		this.height = height;
		this.width = width;
		
		this.filledTiles.put("p1", new HashMap<Integer, TileUI>());
		this.filledTiles.put("p2", new HashMap<Integer, TileUI>());

		TileObserver tileObserver = new TileObserver(this);

		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				// ((row * getRowWidth()) + column)
				int weight = 0;
				if ((row == 0 || row == height - 1) && (col == 0 || col == width - 1)) {
					weight = 20;
				}
				else if ((row == 0 || row == 1 || row == height - 1 || row == height - 2) && (col == 0 || col == 1 || col == width - 1 || col == width - 2))
				{
					weight = 1;
				}
				else if (row == 0 || row == height - 1 || col == 0 || col == width - 1) {
					weight = 10;
				}
				else if (row == 1 || row == height - 2 || col == 1 || col == width - 2) {
					weight = 2;
				}
				else {
					weight = 5;
				}

				TileUI tileUI = new TileUI((row * this.width) + col, weight);
				tileUI.registerObserver(tileObserver);
				this.gameBoard.put(tileUI.getIndex(), tileUI);
			}
		}
	}
	
	/***
	 * Get the current filledTiles HashMap
	 * 
	 * @return HashMap<String, HashMap<Integer, Tile>> - filledTiles
	 * @author mobieljoy12
	 */
	public HashMap<String, HashMap<Integer, TileUI>> getFilledTiles(){
		return this.filledTiles;
	}
	
	/***
	 * Get the current previousBoard HashMap
	 * 
	 * @return HashMap<Integer, HashMap<Integer, Tile>> - previousBoard
	 * @author mobieljoy12
	 */
	//public HashMap<Integer, HashMap<Integer, Tile>> getPreviousBoard(){
		//return this.previousBoard;
	//}
	
	/***
	 * Set the moveCounter to a given value
	 * 
	 * @param newCount - New value
	 * @author mobieljoy12
	 */
	public void setMoveCounter(int newCount) {
		this.moveCounter = newCount;
	}

	/**
	 * Method that clears all settings of the board.
	 * 
	 * @author GRTerpstra
	 */
	public void reset() {
		this.gameBoard = new HashMap<Integer, TileUI>();
	}
	
	/***
	 * Get the current move counter
	 * Move 0 is the initial board
	 * 
	 * @return int - Current move counter
	 * @author mobieljoy12
	 */
	public int getMoveCounter() {
		return this.moveCounter;
	}
	
	/***
	 * Increment the move counter
	 * 
	 * @author mobieljoy12
	 */
	public void incMoveCounter() {
		this.moveCounter++;
	}
	
	/***
	 * Empty all the previous board values
	 * 
	 * @author mobieljoy12
	 */
	public void emptyAllPrevious() {
		//this.previousBoard = new HashMap<Integer, HashMap<Integer, Tile>>();
	}

	public List<TileUI> getAvailableOptions(Player player) {
		HashMap<Integer, TileUI> availableOptions = new HashMap<Integer, TileUI>();
		for(TileUI tileUI : this.getGameBoard().values()) {
			if(tileUI.getOccupant() == player) {
				for(int i = 0; i < 8; i++) {
					TileUI currentTileUI = tileUI;
					int directionOffset = getDirectionOffset(i);
					boolean foundOpponentTile = false;
					while(currentTileUI.getIndex() + directionOffset >= 0 && currentTileUI.getIndex() + directionOffset <= 63) {
						if(		(i == 0 && (currentTileUI.getIndex() < 8)) 												||
								(i == 1 && ((currentTileUI.getIndex() < 8) || (currentTileUI.getIndex() + 1) % 8 == 0)) 	||
								(i == 2 && ((currentTileUI.getIndex() + 1) % 8 == 0)) 									||
								(i == 3 && ((((currentTileUI.getIndex() + 1) % 8 == 0)) || (currentTileUI.getIndex() > 55)))||
								(i == 4 && (currentTileUI.getIndex() > 55)) 												||
								(i == 5 && ((currentTileUI.getIndex() > 55) || currentTileUI.getIndex() % 8 == 0)) 			||
								(i == 6 && (currentTileUI.getIndex() % 8 == 0)) 											||
								(i == 7 && ((currentTileUI.getIndex() % 8 == 0) || currentTileUI.getIndex() < 8))
						)
						{
							break;
						}
						currentTileUI = this.getGameBoard().get(currentTileUI.getIndex() + directionOffset);
						if((currentTileUI.getOccupant() == player) || (currentTileUI.getOccupant() == null && !(foundOpponentTile))) {
							break;
						}
						else if(currentTileUI.getOccupant() != player && currentTileUI.getOccupant() != null) {
							foundOpponentTile = true;
							continue;
						}
						else if(currentTileUI.getOccupant() == null && foundOpponentTile) {
							availableOptions.put(currentTileUI.getIndex(), currentTileUI);
							break;
						}
					}
				}
			}
		}
		return new ArrayList<TileUI>(availableOptions.values());
	}

	public Board clone() {
		return new Board((HashMap<Integer, TileUI>) this.gameBoard.clone(), this.height, this.width);
	}
	/***
	 * Save a previous tile to a moveCount
	 * For AI purposes
	 * 
	 * @param moveCount - The move to save the tile to
	 * @param tile - The Tile to save
	 * @param player - The Player to put on the Tile
	 * @author mobieljoy12
	 */
//	public void savePrevious(int moveCount, Tile tile, Player player) {
//		if(!this.previousBoard.containsKey(moveCount)) this.previousBoard.put(moveCount, new HashMap<Integer, Tile>());
//		Tile prevTile = new Tile(tile.getIndex(), tile.getWeight());
//		prevTile.setOccupant(player);
//
//		this.previousBoard.get(moveCount).put(tile.getIndex(), prevTile);
//	}
	
	/***
	 * Save the current board before making a move so it can be reverted back to
	 * 
	 * @author mobieljoy12
	 */
//	public void savePrevious(Tile tile, Player player) {
//		if(!this.previousBoard.containsKey(this.moveCounter)) this.previousBoard.put(this.moveCounter, new HashMap<Integer, Tile>());
//		Tile prevTile = new Tile(tile.getIndex(), tile.getWeight());
//		prevTile.setOccupant(player);
//		this.previousBoard.get(this.moveCounter).put(tile.getIndex(), prevTile);
//	}
	
	/***
	 * Revert the board back to the previous move
	 * 
	 * @param moves - Amount of moves to revert back
	 * @author mobieljoy12
//	 */
//	public void revert(int moves) {
//		//if((this.moveCounter - moves) < 0) moves = this.moveCounter;
//		for(int counter = 0; counter < moves; counter++) {
//			this.decMoveCounter();
//			for(Tile tile : this.previousBoard.get(this.moveCounter).values()) {
//				if(tile.isOccupied()) {
//					this.addFilledTile(tile.getOccupant(), this.gameBoard.get(tile.getIndex()));
//				}else {
//					this.filledTiles.get(this.gameBoard.get(tile.getIndex()).getOccupant().getId()).remove(tile.getIndex());
//				}
//				this.gameBoard.get(tile.getIndex()).setOccupant(tile.getOccupant());
//			}
//			this.previousBoard.remove(this.moveCounter);
//		}
//	}

	/**
	 * Method that returns the gameboard which consists of an ArrayList of tiles.
	 * 
	 * @return ArrayList<Tile> t the gameboard.
	 * @author GRTerpstra
	 */
	public HashMap<Integer, TileUI> getGameBoard(){
		return this.gameBoard;
	}

	/**
	 * Method that returns the height aka number of rows of the board.
	 * 
	 * @return int - the height of the board.
	 * @author GRTerpstra
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Method that returns the width aka number of columns of the board.
	 * 
	 * @return int - the width of the board.
	 * @author GRTerpstra
	 */
	public int getWidth() {
		return this.width;
	}

	/***
	 * Reset the whole filledTiles HashMap
	 * 
	 * @author mobieljoy12
	 */
	public void resetFilledTiles() {
		this.filledTiles.put("p1", new HashMap<Integer, TileUI>());
		this.filledTiles.put("p2", new HashMap<Integer, TileUI>());
	}
	
	/***
	 * Add a Tile to a Player
	 *
	 * @param player - The Player to add it to
	 * @param tileUI - The Tile to add
	 * @author mobieljoy12
	 */
	public void addFilledTile(Player player, TileUI tileUI) {
		String otherPlayerId = PlayerList.getOtherPlayer(player.getId()).getId();
		if(this.filledTiles.get(otherPlayerId).containsKey(tileUI.getIndex())) {
			this.filledTiles.get(otherPlayerId).remove(tileUI.getIndex());
		}
		this.filledTiles.get(player.getId()).put(tileUI.getIndex(), tileUI);
	}

	/***
	 * Check whether the board is completely full
	 *
	 * @return boolean - Full
	 * @author mobieljoy12
	 */
	public boolean isFull() {
		int filledTileCount = 0;
		for(String pId : this.filledTiles.keySet()) {
			filledTileCount += this.filledTiles.get(pId).size();
		}
		//System.out.println("Full board is: " + (this.getWidth() * this.getHeight()) + " tiles, " + filledTileCount + " are filled");

		return ((this.getWidth() * this.getHeight()) == filledTileCount);
	}

	/***
	 * Get scores of all players
	 *
	 * @return HashMap<Player, Integer> - Hashmap holding scores per player
	 * @author mobieljoy12
	 */
	public HashMap<String, Integer> getScores(){
		HashMap<String, Integer> tempScores = new HashMap<String, Integer>(); // New HashMap
		for(String pId : this.filledTiles.keySet()) {
			tempScores.put(pId, this.filledTiles.get(pId).size());
		}
		return tempScores;
	}

	/***
	 * Get the score for a specific Player
	 *
	 * @param player - The player for which you want the score
	 * @return int - Score in tiles
	 * @author mobieljoy12
	 */
	public int getScore(Player player) {
		return this.filledTiles.get(player.getId()).size();
	}
	
	/***
	 * Get a Tile from the gameboard
	 * Returns null if index is out of bounds
	 * 
	 * @param index - The index for the Tile that is requested
	 * @return Tile or null
	 * @author mobieljoy12
	 */
	public TileUI getTile(int index) {
		return (this.gameBoard.containsKey(index)) ? this.gameBoard.get(index) : null;
	}
	
	/***
	 * Get a Tile from the gameboard
	 * Returns null if index is out of bounds
	 * 
	 * @param row - The row of the Tile requested
	 * @param col - The column of the Tile requested
	 * @return Tile or null
	 * @author mobieljoy12
	 */
	public TileUI getTile(int row, int col) {
		int index = ((row * this.height) + col);
		return (this.gameBoard.containsKey(index)) ? this.gameBoard.get(index) : null;
	}

}

