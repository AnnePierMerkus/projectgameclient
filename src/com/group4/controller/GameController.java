package com.group4.controller;

import java.util.HashMap;

import com.group4.model.GameOptions;
import com.group4.util.Player;

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
	
	// Save the players into a map with Id => Player object
	public static HashMap<String, Player> players = new HashMap<String, Player>();
	
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
	 */
	public int toggleTurn() {
		if(this.game != null) { // No player currently has the turn
			return this.game.toggleTurn();
		}
		return -1;
	}
	
	/***
	 * Get Player
	 * If multiplayer, p1 can be cast to network player
	 * E.g. NetworkPlayer networkPlayer = (NetworkPlayer) GameOptions.getPlayer("p1");
	 * 
	 * @param id - String playerId
	 * @return Player - The player found, or NULL
	 * @author mobieljoy12
	 */
	public Player getPlayer(String id) {
		if(players.containsKey(id)) {
			return players.get(id);
		}
		return null;
	}
	
	/***
	 * Get all players
	 * 
	 * @return HashMap<String, Player> - HashMap with all players with id
	 * @author mobieljoy12
	 */
	public HashMap<String, Player> getPlayers(){
		return players;
	}
	
	/***
	 * Make sure all players are no longer in game
	 */
	public void cleanUp() {
		players.values().forEach((p) -> p.setGameProperty(null));
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
