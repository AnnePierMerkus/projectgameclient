package com.group4.util.observers;

import com.group4.controller.GameController;

public class PlayerObserver implements Observer {

	private GameController gameController;
	
	/***
	 * PlayerObserver lets controller know when player has done a turn
	 * 
	 * @param gameController
	 * @author mobieljoy12
	 */
	public PlayerObserver(GameController gameController) {
		this.gameController = gameController;
	}
	
	@Override
	public void update(Object object) {
		gameController.toggleTurn();
	}

}
