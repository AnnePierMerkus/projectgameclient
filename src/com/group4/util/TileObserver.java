package com.group4.util;

import com.group4.model.Board;
import com.group4.util.observers.Observer;

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
