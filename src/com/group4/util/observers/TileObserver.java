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
		Tile tile = (Tile) object;
		if (tile.isOccupied()) {
			this.board.addFilledTile(tile.getOccupant(), tile);
		}
	}

}
