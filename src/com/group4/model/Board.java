package com.group4.model;

import java.util.List;

import com.group4.util.Tile;

public class Board {
	private List<Tile> gameBoard;
	
	public void create(int height, int width) {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				this.gameBoard.add(j, new Tile(i*j));
			}
		}
	}
}

