package com.group4.controller;

import com.group4.model.GameOptions;

public abstract class GameController extends Controller {
	
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
	protected GameOptions game = null;
	
	protected GameController() {
		super();
	}
	
	/***
	 * 
	 * 
	 * @return GameOptions - Instance for the game that is currently going on
	 * @author mobieljoy12
	 */
	public GameOptions getOptions() {
		return this.game;
	}
	
	/***
	 * Create a new game with automatic difficulty or multiplayer in mind
	 * Abstract so the Singeplayer and Multiplayer can have their own implementation
	 * 
	 * @param gameType
	 * @author mobieljoy12
	 */
	abstract void createGame(GameType gameType);
	
	/***
	 * Create a new game
	 * Abstract so the Singeplayer and Multiplayer can have their own implementation
	 * 
	 * @param difficulty
	 * @param gameType
	 * @author mobieljoy12
	 */
	abstract void createGame(Difficulty difficulty, GameType gameType);
	
	/***
	 * End the current game
	 * Abstract so the Singeplayer and Multiplayer can have their own implementation
	 * 
	 * @author mobieljoy12
	 */
	abstract void endGame();
	
}
