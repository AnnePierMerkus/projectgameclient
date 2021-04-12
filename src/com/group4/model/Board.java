package com.group4.model;

import java.util.HashMap;

import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;
import com.group4.util.observers.TileObserver;

/**
 * Class that creates and defines the game board.
 */
public class Board {

	private HashMap<Integer, Tile> gameBoard = new HashMap<Integer, Tile>();
	private HashMap<String, HashMap<Integer, Tile>> filledTiles = new HashMap<String, HashMap<Integer, Tile>>();
	private HashMap<Integer, HashMap<Integer, Tile>> previousBoard = new HashMap<Integer, HashMap<Integer, Tile>>();
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
	
	/**
	 * Method that creates a new board which size is defined by the given height and width.
	 * @param height the number of rows the board has to contain.
	 * @param width the number of columns the board has to contain.
	 * @author GRTerpstra
	 */
	public Board(int height, int width) {
		this.height = height;
		this.width = width;
		
		this.filledTiles.put("p1", new HashMap<Integer, Tile>());
		this.filledTiles.put("p2", new HashMap<Integer, Tile>());

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

				Tile tile = new Tile((row * this.width) + col, weight);
				tile.registerObserver(tileObserver);
				this.gameBoard.put(tile.getIndex(), tile);
			}
		}
	}

	/**
	 * Method that clears all settings of the board.
	 * @author GRTerpstra
	 */
	public void reset() {
		this.gameBoard = new HashMap<Integer, Tile>();
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
		this.previousBoard = new HashMap<Integer, HashMap<Integer, Tile>>();
	}
	
	/***
	 * Save the current board before making a move so it can be reverted back to
	 * 
	 * @author mobieljoy12
	 */
	public void savePrevious(Tile tile, Player player) {
		if(!this.previousBoard.containsKey(this.moveCounter)) this.previousBoard.put(this.moveCounter, new HashMap<Integer, Tile>());
		Tile prevTile = new Tile(tile.getIndex(), tile.getWeight());
		prevTile.setOccupant(player);
		
		this.previousBoard.get(this.moveCounter).put(tile.getIndex(), prevTile);
	}
	
	/***
	 * Revert the board back to the previous move
	 * 
	 * @param moves - Amount of moves to revert back
	 * @author mobieljoy12
	 */
	public void revert(int moves) {
		for(int counter = 0; counter < moves; counter++) {
			this.decMoveCounter();
			for(Tile tile : this.previousBoard.get(this.moveCounter).values()) {
				if(tile.isOccupied()) {
					this.addFilledTile(tile.getOccupant(), this.gameBoard.get(tile.getIndex()));
				}else {
					Tile onBoardTile = this.gameBoard.get(tile.getIndex());
					if(onBoardTile.isOccupied()) {
						this.filledTiles.get(onBoardTile.getOccupant().getId()).remove(tile.getIndex());
					}
				}
				this.gameBoard.get(tile.getIndex()).setOccupant(tile.getOccupant());
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getHeight(); i++) {
			for (int j = 0; j < getWidth(); j++) {
				if (gameBoard.get(i * j).getOccupant() != null) {
					builder.append("[").append(gameBoard.get(i * j).getOccupant().getId()).append("]");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	/**
	 * Method that returns the gameboard which consists of an ArrayList of tiles.
	 * @return ArrayList<Tile> t the gameboard.
	 * @author GRTerpstra
	 */
	public HashMap<Integer, Tile> getGameBoard(){
		return this.gameBoard;
	}

	/**
	 * Method that returns the height aka number of rows of the board.
	 * @return int - the height of the board.
	 * @author GRTerpstra
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Method that returns the width aka number of columns of the board.
	 * @return int - the width of the board.
	 * @author GRTerpstra
	 */
	public int getWidth() {
		return this.width;
	}

	/***
	 * Add a Tile to a Player
	 *
	 * @param player - The Player to add it to
	 * @param tile - The Tile to add
	 */
	public void addFilledTile(Player player, Tile tile) {
		String otherPlayerId = PlayerList.getOtherPlayer(player.getId()).getId();
		if(this.filledTiles.get(otherPlayerId).containsKey(tile.getIndex())) {
			this.filledTiles.get(otherPlayerId).remove(tile.getIndex());
		}
		this.filledTiles.get(player.getId()).put(tile.getIndex(), tile);
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
		System.out.println("Full board is: " + (this.getWidth() * this.getHeight()) + " tiles, " + filledTileCount + " are filled");
		return ((this.getWidth() * this.getHeight()) == filledTileCount);
	}

	/***
	 * Get scores of all players
	 *
	 * @return HashMap<Player, Integer> - Hashmap holding scores per player
	 */
	public HashMap<Player, Integer> getScores(){
		HashMap<Player, Integer> tempScores = new HashMap<Player, Integer>(); // New HashMap
		for(String pId : this.filledTiles.keySet()) {
			tempScores.put(PlayerList.getPlayer(pId), this.filledTiles.get(pId).size());
		}
		return tempScores;
	}

	/***
	 * Get the score for a specific Player
	 *
	 * @param player - The player for which you want the score
	 * @return int - Score in tiles
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
	public Tile getTile(int index) {
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
	public Tile getTile(int row, int col) {
		int index = ((row * this.height) + col);
		return (this.gameBoard.containsKey(index)) ? this.gameBoard.get(index) : null;
	}

}

