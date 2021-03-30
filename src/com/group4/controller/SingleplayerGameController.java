package com.group4.controller;

import com.group4.model.GameOptions;

public class SingleplayerGameController extends GameController {

	public SingleplayerGameController(boolean multiplayer) {
		super();
	}
	
	/***
	 * Create a new game
	 * 
	 * @param difficulty
	 * @param gameType
	 * @author mobieljoy12
	 */
	public void createGame(Difficulty difficulty, GameType gameType) {
		this.game = new GameOptions(difficulty, gameType);
		//TODO - Swap scene to new game
		this.game.setGameState(GameState.PLAYING);
	}
	
	/***
	 * End the current game
	 * @author mobieljoy12
	 */
	public void endGame() {
		//TODO - Swap scene to end game screen or home menu
		this.game = null;
	}

}
