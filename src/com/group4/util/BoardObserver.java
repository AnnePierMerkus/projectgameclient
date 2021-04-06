package com.group4.util;

import com.group4.model.GameOptions;
import com.group4.util.observers.Observer;

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
