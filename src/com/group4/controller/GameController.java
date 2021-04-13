package com.group4.controller;

import com.group4.games.REVERSI;
import com.group4.model.GameOptions;
import com.group4.util.Player.PlayerState;
import com.group4.util.PlayerList;
import com.group4.util.observers.PlayerObserver;
import javafx.application.Platform;

public abstract class GameController extends Controller {
	
	// The GameTypes possible, a new game should also be added here
	public enum GameType {
		TICTACTOE, REVERSI
	};
	
	// The possible difficulties to add to a game
	public enum Difficulty {
		EASY, MEDIUM, HARD
	};
	
	// Initiate the player observer
	protected PlayerObserver playerObserver = new PlayerObserver(this);
	
	// The game that is currently going on
	protected GameOptions game = null;
	
	/***
	 * The GameController is the base class for all GameControllers, multi- and single-player
	 * 
	 * @author mobieljoy12
	 */
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
			
			if(!this.game.getGameProperty().gameHasEnded()) {
				return this.game.toggleTurn();
			}else {

				Platform.runLater(() -> {
					//go to GameOverScreen
					this.swap(stage, "EndGame.fxml");
					GameOverController gameOverController = (GameOverController) this.getCurrentController();
					if (this.game.getGameProperty().getPlayerWon() == null)
					{
						gameOverController.getResultText().setText("Gelijk Spel!");
					}
					else if (game.getGameProperty().getPlayerWon().getId().equals("p1")){
						gameOverController.getResultText().setText("Je Hebt Gewonnen!");
					}else if (game.getGameProperty().getPlayerWon().getId().equals("p2")){
						gameOverController.getResultText().setText("Je Hebt verloren :(");
					}else {
						gameOverController.getResultText().setText("Gelijk Spel!");
					}

					if (this.game.getGameProperty() instanceof REVERSI) {
						gameOverController.setScoreVisibility(true);
						gameOverController.getScorePlayer1Text().setText("Score Speler 1: " + game.getBoard().getScore(PlayerList.getPlayer("p1")));
						gameOverController.getScorePlayer2Text().setText("Score Speler 2: " + game.getBoard().getScore(PlayerList.getPlayer("p2")));
					}else {
						gameOverController.setScoreVisibility(false);
					}

					gameOverController.getQuitBtn().setOnAction((event) -> {
						this.swap(stage, "../MainMenu.fxml");
					});
				});
				System.out.println("Game over: " + this.game.getGameProperty().getPlayerWon());
				PlayerList.players.values().forEach((p) -> p.setPlayerState(PlayerState.WAITING));
			}
		}
		return "";
	}
	
	/***
	 * Create a new game with automatic difficulty or multi-player in mind
	 * Abstract so the single-player and multi-player can have their own implementation
	 * 
	 * @param gameType
	 * @author mobieljoy12
	 */
	abstract void createGame(GameType gameType);
	
	/***
	 * Create a new game
	 * Abstract so the single-player and multi-player can have their own implementation
	 * 
	 * @param difficulty
	 * @param gameType
	 * @author mobieljoy12
	 */
	abstract void createGame(Difficulty difficulty, GameType gameType);
	
	/***
	 * End the current game
	 * Abstract so the single-player and multi-player can have their own implementation
	 * 
	 * @author mobieljoy12
	 */
	abstract void endGame();
	
}
