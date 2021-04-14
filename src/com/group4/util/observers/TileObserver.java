package com.group4.util.observers;

import com.group4.model.Board;
import com.group4.util.Tile;

public class TileObserver implements Observer {

	private Board board;
	
	public TileObserver(Board board) {
		this.board = board;
	}
	
	@Override
	public void update(Object object) {

	}
	
	public void updateFilledTile(Tile tile, int threadId) {
		if (tile.isOccupied()) {
			this.board.addFilledTile(threadId, tile.getOccupant(), tile);
		}
	}

}
