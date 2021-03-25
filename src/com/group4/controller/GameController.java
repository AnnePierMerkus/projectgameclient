package com.group4.controller;

import com.group4.model.Board;
import com.group4.model.GameOptions;

public class GameController {
	
	public enum GameType {
		TICTACTOE, REVERSI
	};
	
	public enum Difficulty {
		EASY, MEDIUM, HARD
	};
	
	public enum GameState {
		PREPARING, PLAYING, ENDED
	}
	
	// The game that is currently going on
	private GameOptions game = null;
	
	// Multiplayer or not
	private boolean multiplayer;

	public GameController(boolean multiplayer) {
		this.multiplayer = multiplayer;
	}
	
	public void createGame(Difficulty difficulty, GameType gameType) {
		this.game = new GameOptions(difficulty, gameType);
		//TODO - Swap scene to new game
		this.game.setGameState(GameState.PLAYING);
	}
	
	public void endGame() {
		//TODO - Swap scene to end game screen or home menu
		this.game = null;
		Board.reset();
	}
	
	public GameOptions getGameOptions() {
		return this.game;
	}
	
	public boolean isMultiplayer() {
		return this.multiplayer;
	}
	
}
