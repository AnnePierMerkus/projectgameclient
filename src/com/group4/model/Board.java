package com.group4.model;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.Tile;

/**
 * Class that creates and defines the game board.
 */
public class Board {
	
	private List<Tile> gameBoard = new ArrayList<Tile>();
	private int height;
	private int width;

	/**
	 * Method that creates a new board which size is defined by the given height and width.
	 * @param height the number of rows the board has to contain.
	 * @param width the number of columns the board has to contain.
	 * @author GRTerpstra
	 */
	public Board(int height, int width) {
		this.height = height;
		this.width = width;

		int counter = 0;
		for(int i = 1; i <= height; i++) {
			for(int j = 1; j <= width; j++) {
				Tile tile = new Tile(i, j);
				tile.setTranslateX((j-1) * 200);
				tile.setTranslateY((i-1) * 200);
				this.gameBoard.add(counter, tile);
				counter++;
			}
		}
	}
	
	/**
	 * Method that clears all settings of the board.
	 * @author GRTerpstra
	 */
	public void reset() {
		this.gameBoard = new ArrayList<Tile>();
	}
	
	/**
	 * Method that returns the gameboard which consists of an ArrayList of tiles. 
	 * @return ArrayList<Tile> t the gameboard.
	 * @author GRTerpstra
	 */
	public List<Tile> getGameBoard(){
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
	
	//TODO - Add methods to get & change Tiles

}

