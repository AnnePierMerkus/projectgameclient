package com.group4.model;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.Tile;

/**
 * Class that creates and defines the game board.
 */
public class Board {
	
	public static List<Tile> gameBoard = new ArrayList<Tile>();
	private static int height;
	private static int width;	
	
	/**
	 * Method that creates a new board which size is defined by the given height and width.
	 * @param height the number of rows the board has to contain.
	 * @param width the number of columns the board has to contain.
	 */
	public static void create(int height, int width) {
		Board.height = height;
		Board.width = width;
		int counter = 0;
		for(int i = 1; i <= height; i++) {
			for(int j = 1; j <= width; j++) {
				Tile tile = new Tile(i, j);
				tile.setTranslateX((j-1) * 200);
				tile.setTranslateY((i-1) * 200);
				Board.gameBoard.add(counter, tile);
				counter++;
			}
		}
	}
	
	/**
	 * Method that clears all settings of the board.
	 */
	public static void reset() {
		gameBoard = new ArrayList<Tile>();
	}
	
	/**
	 * Method that returns the gameboard which consists of an ArrayList of tiles. 
	 * @return ArrayList<Tile> t the gameboard.
	 */
	public static List<Tile> getGameBoard(){
		return Board.gameBoard;
	}
	
	/**
	 * Method that returns the height aka number of rows of the board.
	 * @return int - the height of the board.
	 */
	public static int getHeight() {
		return Board.height;
	}
	
	/**
	 * Method that returns the width aka number of columns of the board.
	 * @return int - the width of the board.
	 */
	public static int getWidth() {
		return Board.width;
	}
}

