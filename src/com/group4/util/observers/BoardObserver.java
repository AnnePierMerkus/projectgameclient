package com.group4.util.observers;

import com.group4.model.GameOptions;

public class BoardObserver implements Observer {
	
	GameOptions gameOptions;
	
	public BoardObserver(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}

	@Override
	public void update(Object object) {
		// TODO - Tell the view to update
	}
}
