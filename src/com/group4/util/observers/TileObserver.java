package com.group4.util.observers;

import com.group4.model.Board;
import com.group4.util.TileUI;

public class TileObserver implements Observer {

	private Board board;
	
	public TileObserver(Board board) {
		this.board = board;
	}
	
	@Override
	public void update(Object object) {
		TileUI tileUI = (TileUI) object;
		if (tileUI.isOccupied()) {
			this.board.addFilledTile(tileUI.getOccupant(), tileUI);
		}
	}

}
