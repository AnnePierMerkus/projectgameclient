package com.group4.util.observers;

import com.group4.model.Board;

public class TileObserver implements Observer {

	private Board board;
	
	public TileObserver(Board board) {
		this.board = board;
	}
	
	@Override
	public void update(Object object) {
		this.board.notifyObservers();
	}

}
