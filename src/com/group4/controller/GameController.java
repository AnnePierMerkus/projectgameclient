package com.group4.controller;

import com.group4.model.GameOptions;
import com.group4.util.Player.PlayerState;
import com.group4.util.PlayerList;
import com.group4.util.observers.PlayerObserver;

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
	
	// Initiate the player observer
	protected PlayerObserver playerObserver = new PlayerObserver(this);
	
	// The game that is currently going on
	protected GameOptions game = null;
	
	protected GameController() {
		super();
	}
	
	/***
	 * Get the GameOptions for the game currently going on
	 * Returns null if no game is going on
	 * 
	 * @return GameOptions or null - Instance for the game that is currently going on
	 * @author mobieljoy12
	 */
	public GameOptions getOptions() {
		return this.game;
	}
	
	/***
	 * Returns player index on which player has the turn
	 * 0 for p1, 1 for p2 etc.
	 * Returns negative when no player has the turn
	 * 
	 * @return int - Which player has the turn
	 * @author mobieljoy12
	 */
	public String toggleTurn() {
		if(this.game != null) { // No player currently has the turn
			if(this.game.getGameState().equals(GameState.PLAYING)) {
				return this.game.toggleTurn();
			}else if(this.game.getGameState().equals(GameState.ENDED)) {
				PlayerList.players.values().forEach((p) -> p.setPlayerState(PlayerState.WAITING));
			}
			return "";
		}
		return "";
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
