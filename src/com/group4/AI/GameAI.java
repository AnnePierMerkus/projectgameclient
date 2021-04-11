package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.GameOptions;
import com.group4.util.GameProperty;
import com.group4.util.Player;
import com.group4.util.Tile;

public class GameAI extends GameOptions {
	
	// Holds the GameOptions value
	private GameOptions gameOptions;
	
	public GameAI(GameType gameType) {
		super(gameType);
	}
	
	/***
	 * Update this game from the actual game that is going on
	 * 
	 * @author mobieljoy12
	 */
	public void updateFromGame() {
		for(Tile tile : this.gameOptions.getBoard().getGameBoard().values()) {
			this.board.getTile(tile.getIndex()).setOccupant(tile.getOccupant());
		}
		this.playerTurn = this.gameOptions.getPlayerTurn();
	}
	
	/***
	 * Set the GameOptions
	 * 
	 * @param gameOptions
	 * @author mobieljoy12
	 */
	public void setGameOptions(GameOptions gameOptions) {
		this.gameOptions = gameOptions;
	}
	
	/***
	 * Get the GameProperty
	 * 
	 * @return GameProperty
	 */
	public GameProperty getGame() {
		return this.gameOptions.getGameProperty();
	}
	
	/***
	 * Add a move to the prediction board
	 * 
	 * @param tileIndex - The index of the Tile to put the move on
	 * @param player - The Player to set to the tile
	 * @author mobieljoy12
	 */
	public void makePredictionMove(int tileIndex, Player player) {
		this.board.getTile(tileIndex).setOccupant(player);
	}
	
	@Override
	public String toggleTurn() {
		return (this.playerTurn.equals("p1")) ? "p2" : "p1";
	}
	
}
