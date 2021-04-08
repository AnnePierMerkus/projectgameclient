package com.group4.model;

import java.util.ArrayList;
import java.util.HashMap;
import com.group4.util.Tile;
import com.group4.util.TileObserver;
import com.group4.util.observers.Observable;
import com.group4.util.observers.Observer;

/**
 * Class that creates and defines the game board.
 */
public class Board implements Observable {
	
	private HashMap<Integer, Tile> gameBoard = new HashMap<Integer, Tile>();
	private int height;
	private int width;
	private ArrayList<Observer> boardObservers;
	private TileObserver tileObserver = new TileObserver(this);

	/**
	 * Method that creates a new board which size is defined by the given height and width.
	 * @param height the number of rows the board has to contain.
	 * @param width the number of columns the board has to contain.
	 * @author GRTerpstra
	 */
	public Board(int height, int width) {
		this.boardObservers = new ArrayList<Observer>();
		this.height = height;
		this.width = width;
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				// ((row * getRowWidth()) + column)
				int index = (row * this.width) + col;
				Tile tile = new Tile(index);
				tile.registerObserver(this.tileObserver);
				this.gameBoard.put(index, tile);
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

	@Override
	public void registerObserver(Observer observer) {
		this.boardObservers.add(observer);
		
	}

	@Override
	public void removeObserver(Observer observer) {
		this.boardObservers.remove(observer);		
	}

	@Override
	public void notifyObservers() {
		this.boardObservers.forEach((o) -> o.update(this));	
	}
}

