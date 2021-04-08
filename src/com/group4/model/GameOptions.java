package com.group4.model;

import java.util.concurrent.ThreadLocalRandom;
import com.group4.controller.GameController.Difficulty;
import com.group4.controller.GameController.GameState;
import com.group4.controller.GameController.GameType;
import com.group4.util.BoardObserver;
import com.group4.util.GameProperty;
import com.group4.util.Player.PlayerState;
import com.group4.util.PlayerList;

public class GameOptions {
	
	// Difficulty holding enum value
	private Difficulty difficulty;
	
	// GameType holding enum value
	private GameType gameType;
	
	// GameProperty instance holding the game logic
	private GameProperty game;
	
	// Board instance, holds the board with tiles
	private Board board;
	
	// The boardobserver
	private BoardObserver boardObserver = new BoardObserver(this);
	
	// Which player has the turn, negative if no game is going on
	private int playerTurn = -1;
	
	// Set the default gameState to preparing
	private GameState gameState = GameState.PREPARING;
	
	/**
     * Instanciate GameProperty class for the gametype
     *
     * @param className
     * @param type
     * @return GameProperty subclass
     * @author mobieljoy12
     */
    @SuppressWarnings("deprecation")
    private GameProperty instantiate(final String className, @SuppressWarnings("rawtypes") final Class type) {
    	try {
    		GameProperty newGame = (GameProperty) type.cast(Class.forName(className).newInstance());
    		newGame.setGameOptions(this);
            return newGame;
        } catch (InstantiationException
                | IllegalAccessException
                | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
	
    /***
     * Create a new game with given difficulty + gametype
     * 
     * @param difficulty
     * @param gameType
     * @author mobieljoy12
     */
	public GameOptions(Difficulty difficulty, GameType gameType) {
		
		this.difficulty = difficulty;
		this.gameType = gameType;
		
		if(PlayerList.size() < 2) {
			System.out.println("Need at least two players to play the game");
			//TODO - Throw exception, need at least two players to play the game
		}
		
		// Create the game
		this.game = this.instantiate("com.group4.games." + gameType.toString().toUpperCase(), GameProperty.class);

		// Create board
		this.board = new Board(this.game.getBoardHeight(), this.game.getBoardWidth());
		
		// Setup the board if needed - TODO ~ Test the setup
		this.game.doSetup();
		
		// Give the first player the turn
		this.toggleTurn();

	}
	
	/***
	 * Returns player index on which player has the turn
	 * 0 for p1, 1 for p2 etc.
	 * Returns negative when no player has the turn
	 * 
	 * @return int - Which player has the turn
	 * @author mobieljoy12
	 */
	public int toggleTurn() {
		PlayerList.players.values().forEach((p) -> p.setPlayerState(PlayerState.PLAYING_NO_TURN));
		if(this.playerTurn < 0) { // No player currently has the turn
			int gameBasePlayerStart = this.game.playerStart();
			if(gameBasePlayerStart < 0) { // Game says player start doesn't matter
				this.playerTurn = ThreadLocalRandom.current().nextInt(0, 2);
			}else {
				this.playerTurn = gameBasePlayerStart;
			}
		}else {
			this.playerTurn = (this.playerTurn == 0) ? 1 : 0;
		}
		PlayerList.getPlayer("p"+(this.playerTurn+1)).setPlayerState(PlayerState.PLAYING_HAS_TURN);
		return this.playerTurn;
	}
	
	/***
	 * Get the current player turn
	 * 
	 * @return int - The player index that currently has the turn
	 * @author mobieljoy12
	 */
	public int getPlayerTurn() {
		return this.playerTurn;
	}
	
	/***
	 * Get the game logic for the current game
	 * 
	 * @return GameProperty - the game logic
	 * @author mobieljoy12
	 */
	public GameProperty getGameProperty() {
		return this.game;
	}
	
	/***
	 * Get Game Difficulty
	 * 
	 * @return Difficulty
	 * @author mobieljoy12
	 */
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	/***
	 * Get Game type
	 * 
	 * @return GameType
	 * @author mobieljoy12
	 */
	public GameType getGameType() {
		return this.gameType;
	}
	
	/***
	 * Get the GameState
	 * 
	 * @return GameState - The state the game is in
	 * @author mobieljoy12
	 */
	public GameState getGameState() {
		return this.gameState;
	}
	
	/***
	 * Get the board for the current game
	 * 
	 * @return Board - The board currently in use by the game
	 * @author mobieljoy12
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/***
	 * Set the GameState
	 * 
	 * @param state - The state to set the game to
	 * @author mobieljoy12
	 */
	public void setGameState(GameState state) {
		this.gameState = state;
	}
	
}
