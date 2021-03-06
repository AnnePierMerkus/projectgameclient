package com.group4.controller;

import com.group4.AI.AIPlayer;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;

public class SingleplayerGameController extends GameController {

	// The difficulty to play at
	Difficulty difficulty;
	
	/***
	 * SingleplayerGameController controls the game for Single-player matches
	 * 
	 * @param difficulty - The Difficulty to play at
	 * @author mobieljoy12
	 */
	public SingleplayerGameController(Difficulty difficulty, boolean co_op) {
		
		super();
		this.difficulty = difficulty;
		
		PlayerList.addPlayer(new Player("p1"));
		if (co_op) PlayerList.addPlayer(new Player("p2"));
		else PlayerList.addPlayer(new AIPlayer("p2"));
		PlayerList.players.values().forEach((p) -> p.registerObserver(this.playerObserver));
		
	}
	
	@Override
	void createGame(GameType gameType) { // We need this method for Multiplayer stuff, but it still needs to be implemented here
		this.game = new GameOptions(difficulty, gameType);
		for(Player p : PlayerList.players.values()) {
			if(p instanceof AIPlayer) {
				((AIPlayer) p).setGameOptions(this.game);
			}

			p.setGameProperty(this.game.getGameProperty());
		}
	}
	
	@Override
	public void createGame(Difficulty difficulty, GameType gameType) {

		this.game = new GameOptions(difficulty, gameType);
		for(Player p : PlayerList.players.values()) {
			if(p instanceof AIPlayer) {
				((AIPlayer) p).setGameOptions(this.game);
			}
			p.setGameProperty(this.game.getGameProperty());
		}
	}
	
	@Override
	public void endGame() {
		this.game = null;
	}

}
