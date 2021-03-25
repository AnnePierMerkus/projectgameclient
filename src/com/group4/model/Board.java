package com.group4.model;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.Tile;

public class Board {
	
	public static List<Tile> gameBoard = new ArrayList<Tile>();
	private static int height;
	private static int width;	
	
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
	
	public static void reset() {
		gameBoard = new ArrayList<Tile>();
	}
	
	public static List<Tile> getGameBoard(){
		return Board.gameBoard;
	}
	
	public static int getHeight() {
		return Board.height;
	}
	
	public static int getWidth() {
		return Board.width;
	}
	
}

