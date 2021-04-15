package com.group4.AI;

import com.group4.controller.GameController.GameType;
import com.group4.model.Board;
import com.group4.model.GameOptions;
import com.group4.util.Player;
import com.group4.util.PlayerList;
import com.group4.util.Tile;

public class GameAI extends GameOptions {
	
	// Holds the GameOptions value
	private GameOptions gameOptions;
	
	/***
	 * A mock game that the AI can create to perform the predictions
	 * 
	 * @param gameType - The gameType for the game
	 * @author mobieljoy12
	 */
	public GameAI(GameType gameType) {
		super(gameType);
	}
	
	/***
	 * Update this game from the actual game that is going on
	 * 
	 * @author mobieljoy12
	 */
	public void updateFromGame() {
		// Update the board
		for(Tile tile : this.gameOptions.getBoard().getGameBoard().values()) {
			this.board.getTile(tile.getIndex()).setOccupant(tile.getOccupant());
		}
		
		// Update the previous board
		this.board.emptyAllPrevious();
		for(int moveCount : this.gameOptions.getBoard().getPreviousBoard().keySet()) {
			for(Tile tile : this.gameOptions.getBoard().getPreviousBoard().get(moveCount).values()) {
				this.board.savePrevious(moveCount, tile, tile.getOccupant());
			}
		}
		
		// Update the filled tiles
		this.board.resetFilledTiles();
		for(Player p : PlayerList.players.values()) {
			for(Tile tile : this.gameOptions.getBoard().getFilledTiles().get(p.getId()).values()) {
				this.board.addFilledTile(p, tile);
			}
		}
		
		// Update the moveCounter
		this.board.setMoveCounter(this.gameOptions.getBoard().getMoveCounter());
		
		// Update the playerturn
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
	 * Get the current GameOptions
	 * 
	 * @return GameOptions
	 * @author mobieljoy12
	 */
	public GameOptions getGame() {
		return this.gameOptions;
	}

	/***
	 * Add a move to the prediction board
	 * 
	 * @param tileIndex - The index of the Tile to put the move on
	 * @param player - The Player to set to the tile
	 * @author mobieljoy12
	 */
	public void makePredictionMove(int tileIndex, Player player) {
		//this.board.getTile(tileIndex).setOccupant(player);
		this.getGameProperty().makeMove(this.board.getTile(tileIndex), player);
		//this.getGame().makeMove(this.board.getTile(tileIndex), player);
	}

	/***
	 * Add a move to the prediction board
	 *
	 * @param tileIndex - The index of the Tile to put the move on
	 * @param player - The Player to set to the tile
	 * @author mobieljoy12
	 */
	public void makePredictionMove(int tileIndex, Player player, Board board) {
		//this.board.getTile(tileIndex).setOccupant(player);
		this.getGameProperty().makeMove(board.getTile(tileIndex), player, board);
		//this.getGame().makeMove(this.board.getTile(tileIndex), player);
	}
	
	@Override
	public String toggleTurn() {
		return (this.playerTurn.equals("p1")) ? "p2" : "p1";
	}
	
}
