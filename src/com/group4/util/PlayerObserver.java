package com.group4.util;

import com.group4.controller.GameController;
import com.group4.util.observers.Observer;

public class PlayerObserver implements Observer {

	private GameController gameController;
	
	public PlayerObserver(GameController gameController) {
		this.gameController = gameController;
	}
	
	@Override
	public void update(Object object) {
		gameController.toggleTurn();
	}

}
