package com.group4.util.observers;

import com.group4.controller.GameController;
import com.group4.util.PlayerList;

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

		//set UI player turn image
		if (this.gameController.getOptions().getPlayerTurn().equals("p1")){
			gameController.setTurnImage("p2");
		}else{
			gameController.setTurnImage("p1");
		}

		//set UI scores and player available moves
		gameController.setAvailableMovesPlayer1("Available moves: \n" + PlayerList.getPlayer("p1").getAvailableOptions(0).size());
		gameController.setAvailableMovesPlayer2("Available moves: \n" + PlayerList.getPlayer("p2").getAvailableOptions(0).size());
		gameController.setScorePlayer1(String.valueOf(this.gameController.getOptions().getBoard().getScore(0, PlayerList.getPlayer("p1"))));
		gameController.setScorePlayer2(String.valueOf(this.gameController.getOptions().getBoard().getScore(0, PlayerList.getPlayer("p2"))));
	}

}
