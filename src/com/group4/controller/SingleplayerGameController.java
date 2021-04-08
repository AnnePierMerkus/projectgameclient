package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;

public class SingleplayerGameController extends GameController {

	public SingleplayerGameController() {
		super();
		PlayerList.addPlayer(new Player("p1"));
		PlayerList.addPlayer(new Player("p2"));
		PlayerList.players.values().forEach((p) -> p.registerObserver(this.playerObserver));
	}
	
	@Override
	void createGame(GameType gameType) { // We need this method for Multiplayer stuff, but it still needs to be implemented here
		this.game = new GameOptions(Difficulty.MEDIUM, gameType);
		PlayerList.players.values().forEach((p) -> p.setGameProperty(this.game.getGameProperty()));
		//TODO - Swap scene to new game using main Controller
		this.game.setGameState(GameState.PLAYING);
	}
	
	@Override
	public void createGame(Difficulty difficulty, GameType gameType) {

		this.game = new GameOptions(difficulty, gameType);
		PlayerList.players.values().forEach((p) -> p.setGameProperty(this.game.getGameProperty()));
		//TODO - Swap scene to new game using main Controller
		this.game.setGameState(GameState.PLAYING);
	}
	
	@Override
	public void endGame() {
		//TODO - Swap scene to end game screen or home menu
		this.game = null;
	}

}
