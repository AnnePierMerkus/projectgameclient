package com.group4.model;

import java.util.ArrayList;
import java.util.List;

import com.group4.util.Tile;

public class Board {
	
	private List<Tile> gameBoard = new ArrayList<Tile>();
	private int height;
	private int width;	
	
	public void create(int height, int width) {
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
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void reset() {
		gameBoard = new ArrayList<Tile>();
	}
	
	public List<Tile> getGameBoard() {
		return this.gameBoard;
	}
}

