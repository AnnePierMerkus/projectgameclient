package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.PlayerObserver;

public class SingleplayerGameController extends GameController {

	public SingleplayerGameController(boolean multiplayer) {
		super();
	}
	
	@Override
	void createGame(GameType gameType) { // We need this method for Multiplayer stuff, but it still needs to be implemented here
		this.game = new GameOptions(Difficulty.MEDIUM, gameType);
		//TODO - Swap scene to new game using main Controller
		this.game.setGameState(GameState.PLAYING);
	}
	
	@Override
	public void createGame(Difficulty difficulty, GameType gameType) {
		this.game = new GameOptions(difficulty, gameType);
		// Register observer in Player
		PlayerObserver pObserver = new PlayerObserver(this);
		this.game.getPlayers().values().forEach((p) -> p.registerObserver(pObserver));
		//TODO - Swap scene to new game using main Controller
		this.game.setGameState(GameState.PLAYING);
	}
	
	@Override
	public void endGame() {
		//TODO - Swap scene to end game screen or home menu
		this.game = null;
	}

}
